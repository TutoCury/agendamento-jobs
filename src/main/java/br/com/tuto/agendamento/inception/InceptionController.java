package br.com.tuto.agendamento.inception;

import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inception")
public class InceptionController {

    @GetMapping("/dream")
    public ResponseEntity<InceptionHolder> justADream() {
	return ResponseEntity.ok(new InceptionHolder("It's just a dream!"));
    }

    @PostMapping("/whoYouGonnaCall")
    public ResponseEntity<?> whoYouGonnaCall(@RequestBody CallRequestHolder holder) {
	if (new Random().nextBoolean()) {
	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		    .body("Erro aleatório muahahahah!");
	}

	String who = (holder != null && "GC".equalsIgnoreCase(holder.getName())) ? "GhostBusters" : "I Don't know";
	return ResponseEntity.ok(new InceptionHolder(who));
    }

    @GetMapping("/error")
    public ResponseEntity<?> justAnError() {
	return ResponseEntity.badRequest().body("Errou feio, errou rude!");
    }

}
