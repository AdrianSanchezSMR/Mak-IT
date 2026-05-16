# MakIT git setup — logs all output to git_result.txt
Set-StrictMode -Version Latest
$ErrorActionPreference = "Continue"
Set-Location $PSScriptRoot

$log = Join-Path $PSScriptRoot "git_result.txt"
$remoteUrl = "https://github.com/AdrianSanchezSMR/Mak-IT.git"

function Log($msg) {
    $line = if ($null -eq $msg) { "" } else { "$msg" }
    Add-Content -Path $log -Value $line -Encoding utf8
    Write-Host $line
}

"" | Set-Content -Path $log -Encoding utf8
Log "=== MakIT Git Setup $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss') ==="
Log "Working directory: $(Get-Location)"

if (-not (Test-Path .git)) {
    Log "--- git init ---"
    git init 2>&1 | ForEach-Object { Log $_ }
} else {
    Log "--- .git already exists, skipping init ---"
}

Log "--- git remote ---"
$remotes = @(git remote 2>&1)
if ($remotes -contains "origin") {
    Log "Setting origin URL"
    git remote set-url origin $remoteUrl 2>&1 | ForEach-Object { Log $_ }
} else {
    Log "Adding origin"
    git remote add origin $remoteUrl 2>&1 | ForEach-Object { Log $_ }
}
git remote -v 2>&1 | ForEach-Object { Log $_ }

Log "--- git add -A ---"
git add -A 2>&1 | ForEach-Object { Log $_ }

Log "--- git status (before commit) ---"
git status 2>&1 | ForEach-Object { Log $_ }

Log "--- git commit ---"
$commitMsg = @"
Mak-IT Android Jetpack Compose app from Figma design.

Includes login, dashboard, create challenge, profile and stats screens,
blue Material 3 theme, and logo copied from the TFG folder on build.
"@
git commit -m $commitMsg 2>&1 | ForEach-Object { Log $_ }

Log "--- git branch -M main ---"
git branch -M main 2>&1 | ForEach-Object { Log $_ }
git branch -a 2>&1 | ForEach-Object { Log $_ }

$hash = git rev-parse HEAD 2>&1
Log "COMMIT_HASH: $hash"

Log "--- git fetch origin ---"
git fetch origin 2>&1 | ForEach-Object { Log $_ }

$remoteMain = git ls-remote --heads origin main 2>&1
Log "Remote main check: $remoteMain"

if ($remoteMain -and ($remoteMain -notmatch "fatal|error")) {
    Log "--- git pull --rebase origin main ---"
    git pull --rebase origin main 2>&1 | ForEach-Object { Log $_ }
} else {
    Log "--- Skipping pull (no remote main or fetch failed) ---"
}

Log "--- git push -u origin main ---"
git push -u origin main 2>&1 | ForEach-Object { Log $_ }
$pushExit = $LASTEXITCODE
Log "PUSH_EXIT_CODE: $pushExit"
Log "PUSH_SUCCEEDED: $($pushExit -eq 0)"

Log "--- Final status ---"
git log -1 --oneline 2>&1 | ForEach-Object { Log $_ }
git status 2>&1 | ForEach-Object { Log $_ }
Log "GIT_DIR_EXISTS: $(Test-Path .git)"
Log "=== Done $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss') ==="

exit $pushExit
