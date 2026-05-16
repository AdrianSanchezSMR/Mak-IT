# Copia el logo desde la carpeta TFG (Escritorio) al proyecto Android.
$tfg = Split-Path $PSScriptRoot -Parent
$destDir = Join-Path $PSScriptRoot "app\src\main\res\drawable-nodpi"
$dest = Join-Path $destDir "makit_logo.png"

New-Item -ItemType Directory -Force -Path $destDir | Out-Null

$preferred = @(
    (Join-Path $tfg "makit_logo.png"),
    (Join-Path $tfg "logo.png"),
    (Join-Path $tfg "Logo.png")
)

$copied = $false
foreach ($src in $preferred) {
    if (Test-Path -LiteralPath $src) {
        Copy-Item -LiteralPath $src -Destination $dest -Force
        Write-Host "Logo copiado desde: $src"
        $copied = $true
        break
    }
}

if (-not $copied) {
    $image = Get-ChildItem -LiteralPath $tfg -File -ErrorAction SilentlyContinue |
        Where-Object { $_.Extension -match '^\.(png|jpg|jpeg|webp)$' } |
        Sort-Object LastWriteTime -Descending |
        Select-Object -First 1
    if ($image) {
        Copy-Item -LiteralPath $image.FullName -Destination $dest -Force
        Write-Host "Logo copiado desde: $($image.FullName)"
        $copied = $true
    }
}

if (-not $copied) {
    Write-Error "No hay imagen en $tfg. Coloca makit_logo.png (o logo.png) en la carpeta TFG."
    exit 1
}

$mipmap = Join-Path $PSScriptRoot "app\src\main\res\mipmap-xxxhdpi"
New-Item -ItemType Directory -Force -Path $mipmap | Out-Null
Copy-Item -LiteralPath $dest -Destination (Join-Path $mipmap "ic_launcher.png") -Force
Write-Host "Listo: $dest"
