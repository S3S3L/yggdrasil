package io.github.s3s3l.yggdrasil.boot.test;

import io.github.s3s3l.yggdrasil.boot.old.component.annotation.Atom;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
//@Invocation
@Atom
public class Student implements Comparable<Student>,StudentIface {
	private String name;
	private int score;

	@Override
	public int compareTo(Student o) {
		return this.score - o.getScore();
	}

	@Override
	public String showName(){
		return name;
	}
}
