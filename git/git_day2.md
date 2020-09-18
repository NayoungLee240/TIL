# Git day2

## 원격 저장소(Remote Repository) 활용

### 충돌 상황

> git push 

* 원격저장소의 이력과 로컬 저장소의 저장 이력이 다르다.

```bash
$ git push origin master
To https://github.com/NayoungLee240/remote.git
 ! [rejected]        master -> master (fetch first)
error: failed to push some refs to 'https://github.com/NayoungLee240/remote.git'
#rejected(거절)-원격 저장소의 작업이 로컬에 없다.
hint: Updates were rejected because the remote contains work that you do
hint: not have locally. This is usually caused by another repository pushing
# push하기 전에 원격저장소의 변경사항을 먼저 통합(integrate)
hint: to the same ref. You may want to first integrate the remote changes
hint: (e.g., 'git pull ...') before pushing again.
hint: See the 'Note about fast-forwards' in 'git push --help' for details.
```

* 해결 방법 pull

```bash
$ git pull origin master
# vim창으로 commit messege를 작성하게 된다. > 저장 후 종료(:wq)
$ git log --oneline
# merge 된 것이 log로 추가
ea24d9d (HEAD -> master) Merge branch 'master' of https://github.com/NayoungLee240/remote into master
98ce40f Add local.txt
c88eba6 (origin/master) Create new.txt
c0b718c First commit
```

### 저장소 복제

* 클론만들기
  * $ git init과 같은 역할 이다.

```bash
$ git clone https://github.com/NayoungLee240/remote.git
```

* 폴더를 만들기 때문에 cd 를 이용하여 폴더로 들어가 작업한다.
* 새로 만들어진 작업영역이므로 git config --global을 이용하여 사용자 설정을 해준다.

## 브랜치(Branch)

> 독립적인 작업 공간/흐름을 만들기 위해
>
> 가지를 나누어 각각 작업한다. ex) develop(개발), hotfixes(버그수정)

```bash
# 브랜치 추가
$ git branch test

$ git branch
* master
  test

$ git checkout test
Switched to branch 'test'

```



* 브랜치 목록

  ```bash
  $ git branch
  * master
    test
  ```

* 브랜치 이동

  ```bash
  $ git checkout test
  Switched to branch 'test'
  ```

  

* 브랜치 생성 및 이동

  ```bash
  $ git checkout -b test
  Switched to a new branch 'test'
  (test) $
  ```

* 브랜치 병합

  ```bash
  $ git merge test
  ```

* 브랜치 삭제

  ```bash
  $ git branch -d test
  ```

  

#### 팀플에서 일어날 수 있는 상황

1. 내가 만든 PPT가 전부
2. PPT A(1~10p)와 PPT B(11~20p)를 그냥 합친다.
3. PPT A와 PPT B가 같은 슬라이드를 수정

[해설](./branch.md)

