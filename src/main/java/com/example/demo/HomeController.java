package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    ToDoItemRepository toDoItemRepository;

    @Autowired
    UserRepository userRepository;

    @RequestMapping(value="/register", method = RequestMethod.GET)
    public String showRegistrationPage(Model model){
        model.addAttribute("user", new User());
        return "registration";
    }
    @RequestMapping(value="/register", method = RequestMethod.POST)
    public String processRegistrationPage(
            @Valid @ModelAttribute("user") User user,
            BindingResult result,
            Model model){

        model.addAttribute("user", user);
        if (result.hasErrors()) {
            return "registration";
        }else {
            userService.saveUser(user);
            model.addAttribute("message",
                    "User Account Successfully Created");
        }
        return "index";
    }
    @RequestMapping("/")
    public String index(){
        return "index";
    }
    @RequestMapping("/login")
    public String login(){
        return "login";
    }
    @RequestMapping("/secure")
    public String secure(HttpServletRequest request,
                         Authentication authentication,
                         Principal principal) {
        Boolean isAdmin = request.isUserInRole("ADMIN");
        Boolean isUser = request.isUserInRole("USER");
        UserDetails userDetails = (UserDetails)
                authentication.getPrincipal();
        String username = principal.getName();
        return "secure";
    }

        @RequestMapping("/toDoItem")
                public String toDoItems(Model model){
        //you have to Autowire it in the to to use the repo or any other page in here
        model.addAttribute("toDoItems", toDoItemRepository.findAll());
        return "list";
        }

//    @RequestMapping("/addToDoItem")
//    public String searchByName(Model model) {
//        String username = getUser().getUsername();
//        model.addAttribute("user", toDoItemRepository.findByUsername(username));
//        return "descriptionForm";
//
//    }
    private User getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentusername = authentication.getName();
        User user = userRepository.findByUsername(currentusername);
        return user;
    }
//    @PostMapping("/processToDoItem")
//
//    private String  processForm(Model model){
//        @PostMapping("/process")
//        public String processForm(@Valid Job job, BindingResult result){
//            if (result.hasErrors()){
//                return "jobform";
//            }
//            jobRepository.save(job);
//            return "redirect:/";
@RequestMapping(value="/addToDoItem", method = RequestMethod.GET)
public String showToDoItem( Model model){
    model.addAttribute("toDoItem", new ToDoItem());
    return "descriptionform";
}
    @PostMapping("/processToDoItem")
    public String processDescriptionPage(
             @ModelAttribute ToDoItem toDoItem,
            BindingResult result,
            Model model) {
           String username = getUser().getUsername();
           toDoItem.setUsername(username);
           toDoItemRepository.save(toDoItem);
        model.addAttribute("toDoItems", toDoItemRepository.findByUsername(username));
        return "list";

    }


    }


