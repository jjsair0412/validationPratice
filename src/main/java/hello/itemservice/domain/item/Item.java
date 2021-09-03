package hello.itemservice.domain.item;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
//@ScriptAssert(lang = "javascript", script = "_this.price * _this.quantity>=10000",message = "총 합이 만원 넘게 입력해주세요")
public class Item {

    /**
    검증 어노테이션의 value값으로 groups가 있다.
    여기에 검증을 원하는 특정 그룹만 지정해서 넣어준다.
    **/
     @NotNull(groups = UpdateCheck.class) // 수정 요구사항
    private Long id;

     /**
    여러 그룹들이 모두 가져야하는 검증 조건은, 아래처럼 {} 안에 여러개를 넣어둘 수 도 있다.
    **/
     @NotBlank(groups = {SaveCheck.class, UpdateCheck.class})
    private String itemName;

    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
    @Range(min=1000, max = 1000000, groups = {SaveCheck.class, UpdateCheck.class})
    private Integer price;

    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
    @Max(value = 9999, groups = {SaveCheck.class})
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
