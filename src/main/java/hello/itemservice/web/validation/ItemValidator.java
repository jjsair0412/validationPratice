package hello.itemservice.web.validation;


import hello.itemservice.domain.item.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;

import java.lang.annotation.Annotation;

/**
 * 스프링이 지원하는 Validator 인터페이스를 implements 한다..
 */
@Slf4j
@Component
public class ItemValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        /**
         * 파라미터로 넘어오는 class가 item클래스에 지원하느냐를 판단한다.
         * item클래스의 자식클래스가 넘어오는 경우에도 통과한다.
         *
         * item클래스는 프로퍼티이다.
         *
         * 물론 item==clazz 이렇게 작성해도 되지만,
         * isAssignableFrom을 사용한다면 자식클래스가 넘어오는 경우에도 통과하기 때문에
         * 더 좋다.
         */
        return Item.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        /**
         * 프로퍼티를 케스팅해서 target에 넘겨준다.
         *
         * bindingResult는 Errors 클래스의 자식 클래스이기 때문에,
         * 기존에 컨트롤러에서 검증했던 코드를 그대로 사용할 수 있고
         * BindingResult를 errors로 바꾸기만 하면 된다.
         */
        Item item = (Item) target;

        if (!StringUtils.hasText(item.getItemName())) {
            errors.rejectValue("itemName", "required");
        }

        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            errors.rejectValue("price","range",new Object[]{1000,1000000},null);
        }
        if (item.getQuantity() == null || item.getQuantity() > 10000) {
            errors.rejectValue("quantity","max",new Object[]{9999},null);
        }
        //특정 필드 예외가 아닌 전체 예외
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                errors.reject("totalPriceMin",new Object[]{10000,resultPrice},null);
            }
        }

    }
}
