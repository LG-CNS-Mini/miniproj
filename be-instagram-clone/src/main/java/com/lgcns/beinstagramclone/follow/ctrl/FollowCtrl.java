package com.lgcns.beinstagramclone.follow.ctrl;

import com.lgcns.beinstagramclone.follow.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/follow")
public class FollowCtrl {

    @Autowired
    private FollowService followService;

    @PostMapping("/{followerEmail}/follow/{followingEmail}")
    public ResponseEntity<Void> follow(
            @PathVariable String followerEmail,
            @PathVariable String followingEmail) {
        followService.follow(followerEmail, followingEmail);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{followerEmail}/unfollow/{followingEmail}")
    public ResponseEntity<Void> unfollow(
            @PathVariable String followerEmail,
            @PathVariable String followingEmail) {
        followService.unfollow(followerEmail, followingEmail);
        return ResponseEntity.ok().build();
    }
}
