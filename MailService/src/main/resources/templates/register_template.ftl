<!DOCTYPE html>
<html lang="tr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>E-posta Adresinizi Doğrulayın</title>
</head>
<body style="font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 20px;">
<table style="max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border-radius: 5px;">
    <tr>
        <td style="text-align: center;">
            <h2 style="color: #333333;">FinFlex'e Hoşgeldiniz!</h2>
            <p style="color: #666666;">Kaydolduğunuz için teşekkür ederiz.</p>
            <p style="color: #666666;">Lütfen kayıt işleminizi tamamlamak için e-posta adresinizi doğrulayın.</p>
        </td>
    </tr>
    <tr>
        <td style="text-align: center;">
            <p style="color: #000;">Kullanıcı Adı: ${username}</p>
            <p style="color: #000;">Şifre: ${password}</p>
        </td>
    </tr>
    <tr>
        <td style="text-align: center;">
            <p style="color: #666666;">E-posta adresinizi doğrulamak için aşağıdaki butona tıklayın:</p>
            <a href="${link}" style="display: inline-block; padding: 10px 20px; background-color: #28a745; color: #ffffff; text-decoration: none; border-radius: 5px;">E-posta Adresini Doğrula</a>
        </td>
    </tr>
    <tr>
        <td style="padding:5px 0; text-align:center">
            <p style="color: #666666;">Buton çalışmazsa, lütfen bu bağlantıyı kullanın.</p>
            <a href="${link}">${link}</a>
        </td>
    </tr>
    <tr>
        <td style="text-align: center; padding: 10px 0; border-top: 1px solid #eeeeee;">
            <p style="color: #999999; font-size: 12px;">&copy; 2024 FinFlex. Tüm hakları saklıdır.</p>
        </td>
    </tr>
</table>
</body>
</html>
