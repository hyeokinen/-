# Getting Start

## 이 프로젝트를 실행하고 싶을 때 ( Localhost 기준 )

##### &#128680; 본 프로젝트는 git clone 시 Root Directory에서 작업한다는 기준으로 작성했습니다.



## 1. Deep Learning 모델 만들기

 	### 1. Dataset 만들기

​	[데이터셋 만드는 방법](./makedataset.md)



 ### 2. train.py 실행

실행하기 전에 ML 폴더에서 터미널에서 다음 명령어를 타이핑해필요한 파이썬 패키지를 설치해준다.

```
pip install -r requirements.txt
```



### 3. model.hdf5, classes.pkl 파일 생성

훈련이 끝나면 model.hdf5, classes.pkl 파일이 생성되는데 이 파일을 MLSERVER 폴더에 복사해준다.



##### &#9995; 1번 Deep Learning 모델 만들기를 생략하고 싶을 때



## 2.  FE
### 1. axios 요청 시 baseurl 변경
  `FRONTEND/hhfe/src/index.js` 내부의 `axios.defaults.baseURL`의 값을 `http://localhost:9090/api/`으로 바꾼다. ( spring도 로컬로 돌아간다는 가정하에 )

​    

### 2. 다음 파일들의 내부의 `frouturl` 변수 값을 `http://localhost:8000` 으로 바꾼다. (ML SERVER로 보냄)

    `FRONTEND/hhfe/src/components/mainpage/homework/FileUpload.js`
    
    `FRONTEND/hhfe/src/components/mainpage/homework/HomeworkContent.js`
    
    `FRONTEND/hhfe/src/components/mainpage/homework/ScoreTable.js`
    
    `FRONTEND/hhfe/src/components/mainpage/notification/NoticeInfoTable.js`


​    

### 3. MLSERVER에 보낼 경로 지정
`FRONTEND/hhfe/src/components/mainpage/homework/FileUpload.js`의 `onClick` 함수 내부의 axios.post 요청을 `http://localhost:8000/api/v1/calc/`로 바꾼다.

`FRONTEND/hhfe/src/components/mainpage/notification/NotiAddForm.js` 의 `onClick` 함수 내부의 **두 번째** axios.post 요청을 `http://localhost:8000/api/v1/addnoti/` 로 바꾼다.

   

### 4. 서버 실행

서버 실행 전에 필요한 package 설치를 위해 다음과 같은 명령어를 터미널에 친다. 

작업 폴더 위치 (FRONTEND/hhfe)


```
npm install 
```

설치가 왼료되면 다음과 같은 명령어를 터미널에 쳐 개발용 서버를 실행한다.

```
npm start
```



## 3. BE (Spring Boot)

### 1. 스프링 서버 빌드

##### &#10024; [스프링 서버 빌드하는 법](https://lts0606.tistory.com/237)

위의 링크에서 스프링 서버를 빌드한다.



### 2. 스프링 서버 실행

다음 명령어를 타이핑해 스프링을 실행한다.

```
java -jar BootWeb-~~~ .war
```



### 4. BE ( Django )

실행하기 전에 MLSERVER 폴더에서 터미널에서 다음 명령어를 타이핑해필요한 파이썬 패키지를 설치해준다.

```
pip install -r requirements.txt
```

그 후에 Django server를 실행시킨다.

```
python manage.py runserver
```



## 5. 핵심 기능 실행하기

### 1. 교사 아이디 생성 후 로그인하기

![registerteacher](./img/register.JPG)

### 2.  숙제 내기

​	로그인하면 달력 페이지에서 시작일을 클릭하면 숙제를 낼 수 있는 팝업이 뜬다.

![homework](./img/homework.JPG)

### 3. 학생 아이디 생성 및 로그인하기

	1. 의 선생님 아이디 만들 때 처럼 회원가입을 하는데 단 회원가입 시 교사인가요 부분을 체크 해제한다.



### 4. 학생 숙제 제출

왼쪽 상단에 제출 현황 탭을 누르면 선생님이 숙제를 낸 목록을 볼 수 있고 옆에 파일 첨부를 통해 숙제를 재출한다.

![submit](./img/submit.JPG)



### 결과

#### 제출한 파일

![숙제](./SampleImg/1.JPG)

#### 홈페이지 결과
![result](./img/result.JPG)