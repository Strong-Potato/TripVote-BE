# TripVote (8조) 

## ERD 다이어그램 

![Untitled](https://github.com/Strong-Potato/TripVote-BE/assets/32382839/2a38436c-5db7-46dd-9895-eed44fdd4083)

## 피그마링크 

https://www.figma.com/file/oeQ2vOcyO3SuInuMf3Dlj8/%EC%B5%9C%EC%A2%85-%ED%99%94%EB%A9%B4(PM)?type=design&node-id=0-1&mode=design&t=JvKIpNIIdskPNEaP-0

## API 명세 

https://app.swaggerhub.com/apis-docs/strong-potato/trip-vote_api/1.0.0

## 추가적으로 피드백 받고 싶은 부분 작성

- refres-token을 이용해서 access-token을 재발급하는 기능을 구현하고 있습니다.
그런데 refres-token이 만료되면 사용자가 사이트를 이용하던 도중에 세션연결이 끊겨버리게 되는데
그렇게되면 사용자가 결제와 같은 중요한 기능을 수행하는 도중에 연결이 끊겨버릴 수도 있다고 생각을 했습니다.

따라서 refresh-token을 주기적으로 갱신해주는 방법도 고민을 해봤는데, 
access-token과 refresh-token이 함께 탈취되는 경우, 탈취자가 거의 무한정으로 세션을 유지할 수 있는 문제가 있다고 생각이 들었습니다.

보안적으로 안전하고 UX까지 챙길 수 있는 야놀자만의 세션 유지 방법이 있을까요?
