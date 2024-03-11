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

### 로그인 flow
![login_flow](/uploads/43e424386cff33cdace146c760a80e51/image.png)
1. 카카오 로그인을 통해서 회원가입 및 로그인 처리
2. 로그인 후 JWT 토큰 발행
3. 이후 요청에서 JWT 토큰 유효성 검증
4. Access 토큰이 유효한데 Refresh 토큰으로 요청 시 예외처리