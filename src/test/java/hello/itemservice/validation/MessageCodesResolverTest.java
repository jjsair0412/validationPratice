package hello.itemservice.validation;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.ObjectError;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageCodesResolverTest {

    MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();

    @Test
    void messageCodesResolver(){
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item");
        for (String message: messageCodes) {
            /**
             * MessageCodesResolver는 순서대로 required.item과 required 이렇게 출력해준다.
             *
             * 그니까 우선순위가 높은 에러메시지부터 범용성이 높은 낮은 우선순위의 에러코드를 자동으로 탐색해준다.
             *
             * 그리고
             */
            System.out.println("message = " + message);
        }
        assertThat(messageCodes).containsExactly("required.item","required");

    }

    @Test
    void messageCodesResolverField(){
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item", "itemName", String.class);
        for(String message:messageCodes){
            System.out.println("message = " + message);
        }

        /**
         * 결과 :
         * message = required.item.itemName
         * message = required.itemName -> 객체명생략
         * message = required.java.lang.String -> required.타입
         * message = required -> 범용
         *
         * 제일 디테일한 오류코드를 제일먼저 생성해주고, 차례대로 출력해준다.
         *
         * 또한 bindingResult.rejectValue("itemName","required");
         *
         * 이렇게 작성하면 rejectValue안에서는 codesResolver를 호출한다.
         * 그렇다면 해당 test의 결과처럼 네가지 결과를 뽑아오게 된다.
         *
         * 그후 new FieldError를 만들게 되는데, FieldError의 파라미터들중 codes부분에 해당 네가지 결과들을 순서대로 넣어주는거다.
         *
         * 이런식으로 동작하면서 에러코드를 우선순위가 높은것부터 낮은순서대로 rejectValue를 통해서 사용할 수 있는 거다.
         */

        assertThat(messageCodes).containsExactly(
                "required.item.itemName",
                "required.itemName",
                "required.java.lang.String",
                "required"
        );
    }
}
