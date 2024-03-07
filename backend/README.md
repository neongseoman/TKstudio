### KAKAO 로그인
![kakaologin_sequence](/uploads/4bcd41d4046141add0adf996d3e0f139/kakaologin_sequence.png)

### MySQL 한글 설정
- C:\ProgramData\MySQL\MySQL Server 5.7\my.ini 에 아래 코드 추가
```text
[client]
default-character-set=utf8

[mysqld]
character-set-client-handshake = FALSE
init_connect="SET collation_connection = utf8_general_ci"
init_connect="SET NAMES utf8"
character-set-server = utf8

[mysql]
default-character-set=utf8

[mysqldump]
default-character-set = utf8
```