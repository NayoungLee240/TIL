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

   [1~3 설명 및 방법](./branch.md)

* git 에서는 같은 파일이 수정되면 충돌이 발생한다.

### Team

push origin {브렌치}

pull request 브렌치 병합 요청

병합 작업은 충돌에 대해 의사 결정 할 수 있는 사람(팀장)이 해야한다.

* Team 설정 하기

  * 팀장

  setting > manage access > invite access > 같이 할사람 id추가

  * 조원

  초대 수락 : 주소 + /invitations

* Team 으로 branch만들어서 push 해보기

  * 팀장

    ```bash
    $ git checkout -b end
    #작업
    $ git add .
    $ git commit -m ''
    $ git push origin end
    ```

    * github PR보내기

  * 조원

    * PR merge(github)

    ```bash
    $ git checkout -b branch
    #작업
    $ git add .
    $ git commit -m ''
    $ git push origin branch
    ```

    * github PR보내기

  * 팀장

    * PR merge(github)

## Git 명령어 취소

1. `add` 취소

   ```bash
   $ git status
   On branch master
   ```

   ```bash
   $ git restore --staged <파일명>	# 최신
   $ git reset HEAD <파일명>			# 구버전(작년)
   ```

   ```bash
   $ git status
   On branch master
   No commits yet
   Changes to be committed:
     (use "git restore --staged <file>..." to unstage)
           new file:   a.txt
   Untracked files:
     (use "git add <file>..." to include in what will be committed)
           b.txt
   ```

2. `commit` 메시지 변경

   > 주의! 커밋 메시지를 변경하면, hash 값이 변경된다.
   >
   > 즉, 공개된 저장소에 push를 한 이후에는 하지 않는다.

   ```bash
   $git commit --amend
   ```

   * vim 편집기 창에서 직접 메시지를 수정하고 저장

     ```bash
     $ git log --oneline
     
     $ git commit --amend
     
     $ git log --oneline
     ```

   * 강제로 원격 덮어 씌우기

     ```bash
     git push -f origin master
     ```

3. 커밋 변경

   > 이것 역시도 hash값이 변경되기 때문에 주의 한다.

   * 예) 내가 어떠한 파일을 빠뜨리고 커밋 했을 때

     ```bash
     $ git add <빠진 파일>
     $ git commit --amend
     ```

4. `working directory` 변경사항 삭제

   > 주의! 아래의 명령어를 입력하면 절대 과거로 돌아갈 수는 없다.
   >
   > 커밋한 내용만 복구 가능!

   ```bash
   $ git restore .
   ```



## 이력 되돌리기(Reset VS Revert)

* 두 명령어는 특정 시점의 상태로 커밋을 되돌리는 작업을 한다.

  ```bash
  $ git log --online
  <코드번호> 커밋 내용
  $ git reset <코드번호>
  ```

* `reset` : 이력을 삭제

  * `--mixed` : (default) 해당 커밋 이후 변경사항을 보관
  * `--hard` : 해당 커밋 이후 변경사항을 모두 삭제
  * `--soft` : 해당 커밋 이후 변경사항 및 WD 내용까지 보관

* `revert` : 되돌렸다는 이력을 남긴다.

  > 공개된 공간에 push를 이미 했을때

  ```bash
  $ git revert <코드번호>
  ```

  