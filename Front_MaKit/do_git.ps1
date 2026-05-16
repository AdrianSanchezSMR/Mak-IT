Set-Location $PSScriptRoot
$log = Join-Path $PSScriptRoot "git_result.txt"
$remoteUrl = "https://github.com/AdrianSanchezSMR/Mak-IT.git"
function Log($msg) { Add-Content -Path $log -Value $msg; Write-Host $msg }

"" | Set-Content $log
Log "=== MakIT Git $(Get-Date -Format o) ==="

if (-not (Test-Path .git)) {
    Log (git init 2>&1 | Out-String)
}

if (-not (git remote 2>&1 | Select-String -Pattern "^origin$")) {
    Log (git remote add origin $remoteUrl 2>&1 | Out-String)
} else {
    $current = git remote get-url origin 2>&1
    if ($current -ne $remoteUrl) {
        Log (git remote set-url origin $remoteUrl 2>&1 | Out-String)
    }
}

if (-not (Test-Path .gitignore)) {
    @'
*.iml
.gradle
/local.properties
/.idea/
.DS_Store
/build
/captures
.externalNativeBuild
.cxx
*.apk
*.ap_
*.aab
'@ | Set-Content .gitignore -Encoding UTF8
}

Log (git status 2>&1 | Out-String)
Log (git add -A 2>&1 | Out-String)

$commitMsg = @"
Mak-IT Android Jetpack Compose app from Figma design.

Includes login, dashboard, create challenge, profile and stats screens,
blue Material 3 theme, and logo copied from the TFG folder on build.
"@

Log (git commit -m $commitMsg 2>&1 | Out-String)
Log "COMMIT_HASH: $(git rev-parse HEAD 2>&1)"

Log (git fetch origin 2>&1 | Out-String)

$branch = git branch --show-current 2>&1
if (-not $branch -or $branch -match "fatal") {
    $remoteHead = git ls-remote --symref origin HEAD 2>&1 | Select-String "refs/heads/(\S+)"
    if ($remoteHead) {
        $branch = $remoteHead.Matches.Groups[1].Value
    } else {
        $branch = "main"
    }
    Log (git checkout -b $branch 2>&1 | Out-String)
}

$upstream = "origin/$branch"
$behind = git rev-list --count "HEAD..$upstream" 2>$null
if ($behind -match '^\d+$' -and [int]$behind -gt 0) {
    Log (git pull --rebase origin $branch 2>&1 | Out-String)
}

Log (git push -u origin HEAD 2>&1 | Out-String)
Log (git status 2>&1 | Out-String)
Log "DONE"
