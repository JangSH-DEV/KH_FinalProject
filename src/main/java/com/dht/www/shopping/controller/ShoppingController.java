package com.dht.www.shopping.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dht.www.shopping.model.service.ShoppingService;
import com.dht.www.shopping.model.vo.Basket;
import com.dht.www.user.model.vo.Users;

@Controller
@RequestMapping("/shopping")
public class ShoppingController {
	
	@Autowired
	private ShoppingService shoppingService;

	
	//쇼핑 홈
	@RequestMapping(value="/home", method=RequestMethod.GET)
	public void shoppingHome(Model model) {
		
		model.addAttribute("list1", shoppingService.selectHome("A"));
		model.addAttribute("list2", shoppingService.selectHome("B"));
		model.addAttribute("list3", shoppingService.selectHome("C"));
		
		System.out.println(shoppingService.selectHome("A"));
	}
	
	//게시판 목록
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public String shoppingList(Model model, @RequestParam(required=false, defaultValue="1") int curPage
								, @RequestParam(required=false, defaultValue="0") int listno) {
		
		int cntPerPage = 16;
		
		model.addAttribute("list", shoppingService.selectList(listno, curPage, cntPerPage));
		model.addAttribute("listno", listno);
		
		return "/shopping/content";
		
	}
	
	@RequestMapping("/content")
	@ResponseBody
	public void loadContent() {}
	
	//제품 상세페이지
	@RequestMapping(value="/detail", method=RequestMethod.GET)
	public void shoppingDetail(Model model) {
		
	}
	
	//장바구니 조회
	@RequestMapping(value="/basket", method=RequestMethod.GET)
	public void shoppingBasket(Model model, HttpSession session) {
		
		Users user = (Users)session.getAttribute("logInInfo");
		
		if(user != null) {
			model.addAttribute("basket", shoppingService.selectBasket(user));
		} else {
			model.addAttribute("basket", session.getAttribute("basket"));
		}
	}

	//장바구니 추가
	@RequestMapping(value="/basket", method=RequestMethod.POST)
	public void addBasket(Model model, HttpSession session) {
		
		Users user = (Users)session.getAttribute("logInInfo");
		
		//로그인
		if(user != null) {
			
		} else {
			
		}
	}
	
	//장바구니 수량 업데이트
	@RequestMapping(value="/amount", method=RequestMethod.GET)
	@ResponseBody
	public int updateAmount(int amount, String code, HttpSession session) {
		
		Users user = (Users) session.getAttribute("logInInfo");

		//로그인
		if (user != null) {
			Basket basket = new Basket();
			basket.setId(user.getId());
			basket.setCode(code);
			basket.setAmount(amount);
			
			return shoppingService.updateAmount(basket);
			
		} else {

			List<Map<String, Object>> basket = (List<Map<String, Object>>) session.getAttribute("basket");
			
			if (basket.get(0).get("sessionId").equals(session.getId())) {
				Map<String, Object> items = basket.get(1);
				
				Basket item = (Basket) items.get(code);
				item.setAmount(amount);

				return 0;

			} else {
				return -1;
			}
		}
	}
	
	@RequestMapping(value="/payment", method=RequestMethod.GET)
	public void shoppingPayment(Model model, HttpSession session) {
		
//		model.addAttribute("user",shoppingService.selectUserInfo(session.getId()));
		
		String code = "B203";
		//basket 으로 가져오기
		model.addAttribute("product", shoppingService.selectProuct(code));
		
//		Users user = (Users) session.getAttribute("logInInfo");
		
		System.out.println("이거 결과는 뭐야" + shoppingService.selectPoint("semin"));
		
		model.addAttribute("point", shoppingService.selectPoint("semin"));
		//model.addAttribute(attributeValue)
	}
	
	
	@RequestMapping(value="/paymentComplete", method = RequestMethod.POST)
	@ResponseBody
	public void shoppingPaymentComplete(@RequestParam String imp_uid) {
		
		System.out.println("결제" + imp_uid);
	}
	
	@RequestMapping(value="/paymentComplete", method = RequestMethod.GET)
	public void shoppingPaymentCompleteGET(@RequestParam String imp_uid) {
		System.out.println("결제GET" + imp_uid);
	}
	
	
	@RequestMapping(value="/delivery", method=RequestMethod.GET)
	public String shoppingDelivery(@RequestParam int num) {
		
		if(num == 0) {
			return "shopping/delivery_basic";
		}else{
			return "shopping/delivery_new";
		}
		
	}

	@RequestMapping("/test")
	public void test() { }

}
