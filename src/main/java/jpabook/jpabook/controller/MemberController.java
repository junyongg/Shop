package jpabook.jpabook.controller;

import jakarta.validation.Valid;
import jpabook.jpabook.domain.Address;
import jpabook.jpabook.domain.Member;
import jpabook.jpabook.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


    @GetMapping("/members/new")
    public String createForm(Model model){
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("members/new")
    public String create(@Valid MemberForm form, BindingResult result){

        if(result.hasErrors()){
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model) {
        //화면에 필요한 데이터를 찍어서 보여주는 템플릿 엔진(지금 한서버에서 타임리프 사용중)에서는 엔티티를 사용해도 괜찮지만 api를 만들때는 절대 엔티티를 그대로 뿌리면
        //안되고 Form이나 DTO를 만들어서 뿌리는것이 좋다.
        List<Member> members = memberService.findMembers();
        model.addAttribute("members",members);
        return "members/memberList";
    }




}
