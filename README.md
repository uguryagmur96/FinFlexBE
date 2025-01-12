
# FinFlex

FinFlex kurumsal bir döviz alım-satım uygulamasıdır.Arka taraf uygulaması Spring Boot frameworkünde Java diliyle yazılmıştır.
Ön taraf uygulamasında React,JS frameworkünde   JavaScript dili kullanılarak yazılmıştır.
Ayrıca Material UI ve Ant Design'dan yararlanılmıştır.


## Bilgisayarınızda Çalıştırın

 Projenin backend uygulamasını klonlayın

```bash
git clone https://gitea.infina.com.tr/akademi-24/FinFlex.git
```

Projenin frontend uygulamasını klonlayın

```bash
git clone --branch origin https://gitea.infina.com.tr/akademi-24/FinFlex.git
```

Kafka konfigürasyonu

```bash
$ cd Kafka
$ docker-compose up -d
```

İstemci için gerekli paketleri yükleyin

```bash
npm install
```

İstemciyi çalıştırın

```bash
npm start
```

  
## Kullanılan Teknolojiler

**İstemci:** React

**Sunucu:** Spring Boot

**Messaging Queue:** Apache Kafka


**Veritabanı:** Microsoft SQL Server

  
## Ortam Değişkenleri

**Windows için**

Açık verilerimizin saklanması için environment variables kullanıyoruz. Bunun için öncelikle "Sistem ortam değişkenlerini düzenleyin" aratıp tıklayın.

Ortam Değişkenleri  butonuna tıklayın karşınıza çıkan sayfada Sistem Değişkenleri tablosunun altındaki Yeni butonuna basınız

Sonrasında karşımıza gelen ekranda bu bilgileri ekleyip tamam tuşuna basıyoruz. Artık url imiz enviroment variablesta tutuluyor olacak.
```bash
Değişken Adı: RATES_URL
Değişken Değeri: https://v6.exchangerate-api.com/v6/{API_KEY}/pair/
```

**MacOS**

Terminale giriş yapın aşşağıdaki komutu çalıştırın

```bash
$ nano ~/.zshrc
```
Bu kodu dosyaya yapıştırın 
```bash
$ export RATES_URL="https://v6.exchangerate-api.com/v6/{API_KEY}/pair/"
```
Sonrasında kaydetmek için CTRL+X ve Y tuşlarına basın ve Enter'a basın.

Sonrasında terminali yenilemek için aşağıdaki komutu çalıştırın

```bash
$ source ~/.zshrc
```
Aşağıdaki komutu çalıştırarak ortam değişkenini görebilirsiniz

```bash
$ echo $RATES_URL
```




## API Kullanımı

https://app.exchangerate-api.com/

  