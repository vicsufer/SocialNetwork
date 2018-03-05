package com.uniovi.controllers;

import java.security.Principal;
import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.uniovi.entites.FriendshipRequest;
import com.uniovi.entites.User;
import com.uniovi.services.FriendshipRequestService;
import com.uniovi.services.UsersService;

@Controller
public class FriendshipRequestController {

	@Autowired
	FriendshipRequestService friendshipRequestService;
	
	@Autowired
	UsersService userService;
	
	@RequestMapping(value="/friendshipRequest/list")
	public String getRequestList(Model model, Principal principal, Pageable pageable)
	{
		User user = userService.getUserByEmail( principal.getName() );
		Page<FriendshipRequest> request = new PageImpl<FriendshipRequest>(new LinkedList<FriendshipRequest>());
		
		request = friendshipRequestService.getPendingRequestToUser(pageable, user );
		
		model.addAttribute("requestList", request );
		model.addAttribute("page", request);
		
		return "friendshipRequest/list";
	}
	
	@RequestMapping("/friendshipRequest/listFriends")
	public String getListFriends(Model model, Pageable pageable,
			@RequestParam(value = "", required = false) String searchText) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		User activeUser = userService.getUserByEmail(email);

		Page<User> friends = new PageImpl<User>(new LinkedList<User>());

//		if (searchText != null && !searchText.isEmpty()) {
//			users = usersService.searchByNameOrEmail(pageable, searchText, activeUser);
//		} else {
			friends = friendshipRequestService.getFriends(pageable, activeUser);
//		}
		
		model.addAttribute("friendsList", friends.getContent());
		model.addAttribute("page", friends);

		return "friendshipRequest/listFriends";
	}
	
	@RequestMapping(value="/sendRequest", method = RequestMethod.POST)
	public String sendRequest(Model model, Principal principal, @RequestParam Long receiver)
	{
		User sender = userService.getUserByEmail( principal.getName() );
		friendshipRequestService.sendRequest(sender, receiver);	
		return "redirect:/user/list";
	}
	
	@RequestMapping(value="/acceptRequest", method = RequestMethod.POST)
	public String acceptRequest(Model model, Principal principal,@RequestParam Long request)
	{
		User receiver = userService.getUserByEmail( principal.getName() );
		//We give the receiver user to avoid accepting frinedship request of other users (ex: Intercept post and modify body)
		friendshipRequestService.acceptRequest(request, receiver);	
		return "redirect:/friendshipRequest/list";
	}
}
