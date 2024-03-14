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

### 배포 절차

- Redis 설치

```shell
docker network create redis-net

docker pull redis

docker run --name redis-server -p 6379:6379 --network redis-net -d redis redis-server --appendonly yes

docker run -it --network redis-net --rm redis redis-cli -h redis-server
```

- MariaDB 설치

```shell
sudo docker pull mariadb:latest

docker run -d --restart always -p 3306:3306 -e MYSQL_ROOT_PASSWORD=1234 -v /var/lib/mysql:/var/lib/mysql --name mariadb mariadb
```

- 자바 환경변수 설정

```shell
vi /etc/profile

# 가장 아래 쪽에 해당 내용 추가
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$PATH:$JAVA_HOME/bin
export CLASSPATH=$JAVA_HOME/jre/lib:$JAVA_HOME/lib/tools.jar

# 변경내용 적용하기
source /etc/profile
```

- 도커 환경변수 설정하기

```shell
vi .env

# 아래 내용 작성
KAKAO_CLIENT_ID=2c2312493369c3c1b5937c1528806eb9
KAKAO_CLINET_SECRET=gnKAC2CPk1oImOKq4dJptGsgukJYfaMZ
KAKAO_REDIRECT_URI=http://localhost:3000/api/v1/user/login/kakao

MYSQL_URL=jdbc:mysql://j10a101.p.ssafy.io:3306/gallery
MYSQL_USERNAME=root
MYSQL_PASSWORD=1234

REDIS_HOST=j10a101.p.ssafy.io
REDIS_PORT=6379
```

```shell
# 기존 이미지 삭제
docker rmi -f {이미지 ID}

# 프로젝트 빌드
chmod +x ./gradlew
./gradlew wrapper --gradle-version=8.5 --distribution-type=bin
./gradlew clean bootJar

# 프로젝트 이미지 생성
docker build -t gallery:latest .

# 프로젝트 이미지 실행
docker run -i -t -p 8081:8081 --env-file .env -d --name gallery -e TZ=Asia/Seoul gallery:latest

# 도커 컨테이너 삭제
docker rm $(docker ps -a -q --filter "ancestor=이미지이름")

```