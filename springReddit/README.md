# SpringReddit

## Quick start

```bash
# cmd generate jks key
# https://help.gamesalad.com/gamesalad-cookbook/publishing/4-android-publishing/4-02-creating-a-keystore/

keytool -genkey -v -keystore spring_reddit.jks -alias alias_name -keyalg RSA -sigalg SHA1withRSA -keysize 2048 -validity 10000

# pwd : 000000
```

## Ref
- Fake email server
	- https://mailtrap.io/home