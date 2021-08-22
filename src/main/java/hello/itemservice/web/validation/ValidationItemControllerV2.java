package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
@Slf4j
public class ValidationItemControllerV2 {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v2/addForm";
    }

    /**
     BindingResult가 바로 v1에 있던 errors 역할을 대신해준다.

     그니까 에러를 담고있는곳을 대신해준다.
     근데 주의할점은 BindingResult의 파라미터 위치는 ModelAttribute뒤에 와야 한다.
     */
    @PostMapping("/add")
    public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        // 검증 오류 결과를 보관
//        Map<String, String> errors = new HashMap<>();

        //검증 로직
        if (!StringUtils.hasText(item.getItemName())){
            // 받아온 item의 Name에 문자가 없다면
//            errors.put("itemName","상품 이름은 필수입니다.");
            /**
             * 필드 오류에 대해서는 bindingResult.addError에
             * FieldError를 사용하면 된다.
             * 파라미터로는 순서대로 ModelAttribute를 담아준 item과,
             * filed의 이름,
             * 나올 메시지를 작성해준다.
             */
            bindingResult.addError(new FieldError("item","itemName","상품 이름은 필수입니다."));
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice()>1000000){
//            errors.put("price","가격은 1000원에서 1,000,000까지 허용합니다.");
            bindingResult.addError(new FieldError("item","price","가격은 1000원에서 1,000,000까지 허용합니다."));
        }

        if (item.getPrice() == null || item.getQuantity() > 9999){
//            errors.put("quantity","수량은 최대 9,999까지 허용합니다.");
            bindingResult.addError(new FieldError("item","quantity","수량은 최대 9,999까지 허용합니다."));
        }

        // 특정 필드가 아닌 복합적인 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000){
//                errors.put("globalError","가격*수량의 합은 10,000원 이상이야 합니다. 현재 값 = " + resultPrice);
                /**
                 * 특정한 field가 없는 경우에는 ObjectError를 사용하면 된다.
                 * 그니까 global오류는 ObjectError를 사용한다.
                 * 해당 경우엔 파라미터가 순서대로
                 * ModelAttribute로 담아준 object인 item,
                 * 에러 메시지를 작성한다.
                 */
                bindingResult.addError(new ObjectError("item","가격*수량의 합은 10,000원 이상이야 합니다. 현재 값 = " + resultPrice));
            }
        }

        // 검증에 실패하면 다시 입력 폼으로
        // bindingResult에 Error가 존재한다면
        if (/**!errors.isEmpty()**/ bindingResult.hasErrors()){
            // errors map이 텅 비지 않았다면
            log.info("errors={}",bindingResult);
//            model.addAttribute("errors",bindingResult);
//          modelAttribute에 자동으로 BindingResult가 담긴다. 그니까 addAttribute를 안적어줘도 상관없다.
            return "validation/v2/addForm";
        }

        // 검증에 성공하면 ( 에러가 없다면 )
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v2/items/{itemId}";
    }

}

