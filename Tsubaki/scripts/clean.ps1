$folders = @("bootstrap", "common", "core", "storage")

for ($i = 0; $i -lt $folders.Count; $i++) {
    $uwu = $folders[$i]

    Write-Output "cleaning cache for $uwu"
    try {
        Remove-Item -Recurse ./$uwu/build
    } catch [System.Exception] {
        continue
    }
}
