## Git

> 인용문`>`
>
> Git은 분산 버전관리 시스템(DVCS)중 하나이다.

## Git 사전 준비

> git을 사용하기 전에 커밋을 남기는 사람에 대한 정보를 설정(최초)

```bash
$ git config --global user.name 'NayoungLee240'
$ git congig --global user.email 'iny003@naver.com'
```

* 추후에 commit을 하면, 작성한 사람(author)로 저장된다.
* email 정보는 github에 등록된 이메을로 설정을 하는 것을 추천(잔디밭)
* 설정 내용을 확인하기 위해서는 아래의 명령어를 입력한다.

```bash
$ git config --global -l
user.name=NayoungLee240
user.email=iny003@naver.com
```

> git bash 설치 [링크](https://gitforwindows.org/)

## 기초 흐름

> 작업-> add -> commit

### 0. 저장소 설정

```bash
$ git init
Initialized empty Git repository in F:/gittest2/.git/
```

* git 저장소를 만들게 되면 해당 디렉토리 내에 `.git/` 폴더가 생성
* git bash에서는 `(master)` 로 현재 작업중인 브랜치가 표기 된다.

### 1. `add`

> 커밋을 위한 파일 목록(staging area)

```bash
$ git add .				#현재 디렉토리의 모든 파일 및 폴더
$ git add a.txt			#특정 파일
$ git add md-images/	#특정 폴더
```

### 2. `commit`

> 버전을 기록(스냅샷)

```bash
$ git commit -m '커밋메시지'
```

* 커밋 메시지는 현재 버전을 알 수 있도록 명확하게 작성한다.

* 커밋 이력을 남기고 확인하기 위해서는 아래의 명령으를 입력한다.

  ```bash
  $ git log
  $ git log -1
  $ git log --oneline
  $ git log -1 --oneline
  ```

## status-상태 확인

> git에 대한 모든 정보는 status에서 확인할 수 있다.

```bash
$ git status
```


## 추가

```bash
$ touch a.txt
$ git add .	
$ git status
#commit전에
On branch master
No commits yet
Changes to be committed:
  (use "git rm --cached <file>..." to unstage)
        new file:   a.txt
$ touch b.txt
$ git status
On branch master

No commits yet
# staging 단계
Changes to be committed:
  (use "git rm --cached <file>..." to unstage)
        new file:   a.txt
#working directory 단계
Untracked files:
  (use "git add <file>..." to include in what will be committed)
        b.txt

```

## 원격 저장소 활용하기

> 원격 저장소를 제공하는 서비스는 github, gitlab, bitbucket등이 있다.

### 1. 원격 저장소 설정하기

```bash
$ git remote add origin https://github.com/NayoungLee240/git-test.git
$ git push -u origin master
```

### 2. 원격 저장소 확인하기

```bash
$ git remote -v
origin  https://github.com/NayoungLee240/git-test.git (fetch)
origin  https://github.com/NayoungLee240/git-test.git (push)
```

* 원격 저장소를 삭제하기 위해 아래의 명령어를 사용한다.

```bash
$ git remote rm origin
```



### 3. push

```bash
$ git push origin master
```

* origin  원격 저장소의 master 브랜치로 push



가장최신 상태의 환경으로 돌아오기(checkout)

```bash

```

# TIL

> Today I Learned
>
> 1일 1커밋 - 잔디밭 심기

* 로컬에 TIL 폴더를 넣고,

  * 지금까지 작성된 마크다운

    (markdown.md, git.md)

  * 넣어놓고 커밋

* TIL원격 커밋 장소 만들고

  * push

