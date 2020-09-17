# 마크다운 문법

## 제목(heading)

제목은 `#` 으로 표현, 제목의 레벨은`#`의 갯수로 표현, h1~h6 까지 표현된다.

## 목록

목록은 순서가 있는 목록과 순서가 없는 목록으로 구분된다.

1. 순서가 있는 목록

2. 순서가 있는 목록

   1. tab을 통해서 하위 레벨로 진입
   2. 계속 작성

   enter

3. enter

* 순서가 없는 목록 `*`
  * tab
* enter는 똑같음
  * tab
    * tab

1. 섞어서 쓸수도 있습니다
   * 이렇게.

## 코드 블록

``` html
<h1>
    HTML backkick세번
</h1>
```

```java
String java
```

```sql
SELECT * FROM tables;
```

다양한 언어 문법의 syntax highlighting을 지원한다

## 링크

[구글 링크](https://google.com)

## 이미지

![뱁새2](C:\Users\이나영\Desktop\이미지\뱁새2.png)

* 이미지를 상대경로로 자동으로 저장하기 위해서 typora에 다음과 같이 설정
  * 설정>copy image to custom folder
    * 로컬 이미지
    * 온라인 이미지
    * 가능하면 상대적위치
    * auto escape

![뱁새2](md-images/%EB%B1%81%EC%83%882.png)



## 표

| 순번 | 이름   | 비고 |
| ---- | ------ | ---- |
| 1    | 홍길동 |      |
| 2    | 김철수 |      |
| 3    |        |      |

## 기타

**굵게**

*기울임*

~~취소선~~ 물결2개

---

하이픈3개

