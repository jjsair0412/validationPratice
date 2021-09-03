package hello.itemservice.web.validation;

import hello.itemservice.web.validation.form.ItemSaveForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/validation/api/items")
public class ValidationItemApiController {

    /**
     * json으로 값이 컨트롤러로 오기 떄문에, ItemSaveForm에다가 @RequestBody를 붙여줬다.
     *
     * 또한 오는 값에 검증을 진행해야 하기 때문에 @Validated도 붙여주고, 에러결과를 저장할 BindingResult도 작성한다.
     *
     * 이때 ItemSaveForm 검증에 실패했을 경우, 해당 컨트롤러 자체가 실행되지 않는다.
     * 그 이유는 무엇일까 ?
     *
     * 왜냐하면 ItemSaveForm 객체자체를 만들어야 검증이라도한다.
     * 그런데 json으로 오는 값에 타입이 맞지않는다면 @ResquestBody가 json을 객체로 변환해갖고 객체를 만들수도 없어서 검증 자체도 안돼는것이다.
     *
     */
    @PostMapping("/add")
    public Object addItem(@RequestBody @Validated ItemSaveForm form, BindingResult bindingResult){
        log.info("API 컨트롤러 호출");

        if(bindingResult.hasErrors()){
            log.info("검증 오류 발생 error={}",bindingResult);
            /**
             * getAllErrors하면 필드에러나 오브젝트에러 모두다 반환한다.
             * 결과적으로 반환된 에러들이 json으로 생성되어 반환된다.
             *
             * 실제로 개발 할 때는 필요한데이터만 뽑아서 json으로 변환후 반환해야 한다.
             */
            return bindingResult.getAllErrors();
        }

        log.info("성공 로직 실행");
        return form;
    }
}
