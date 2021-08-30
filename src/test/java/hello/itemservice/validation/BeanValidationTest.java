package hello.itemservice.validation;

import hello.itemservice.domain.item.Item;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class BeanValidationTest {

    @Test
    void beanValidation(){
        // 검증기 실행 -> 이렇게 검증기를 실행할 수 있는데, 스프링과 통합하면 이런 코드를 작성하지 않아도 된다.
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validatior = factory.getValidator();

        Item item = new Item();
        item.setItemName(" "); // 공백
        item.setPrice(0);
        item.setQuantity(10000);

        Set<ConstraintViolation<Item>> validate = validatior.validate(item);
        for (ConstraintViolation<Item> violation: validate) {
            System.out.println("violation = " + violation);
            System.out.println("violation.getMessage() = " + violation.getMessage());
        }

    }
}
