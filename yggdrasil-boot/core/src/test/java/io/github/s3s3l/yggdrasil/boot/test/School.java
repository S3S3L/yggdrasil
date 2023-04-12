package io.github.s3s3l.yggdrasil.boot.test;

import io.github.s3s3l.yggdrasil.boot.old.component.annotation.AutoInjected;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
// @Invocation
// @Atom
public class School {

	@AutoInjected("depotStudent")
	public Student student;
}
  