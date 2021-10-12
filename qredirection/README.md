# QRedirection (Avec le QR Code buggé)

![img](0.png)

## Binwalk

- La première version présentait un QR Code buggé qui ne fonctionnait pas donc on peut directement binwalk le fichier :

```
$ binwalk QR_code.png           

DECIMAL       HEXADECIMAL     DESCRIPTION
--------------------------------------------------------------------------------
0             0x0             PNG image, 200 x 200, 8-bit/color RGBA, non-interlaced
54            0x36            Zlib compressed data, compressed
3184          0xC70           PNG image, 1147 x 180, 8-bit/color RGB, non-interlaced
3275          0xCCB           Zlib compressed data, compressed
```

- Une image est caché, donc on peut `binwalk -e` puis la lire pour voir le flag !