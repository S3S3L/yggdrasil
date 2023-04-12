package io.github.s3s3l.yggdrasil.boot.test;

import io.github.s3s3l.yggdrasil.boot.old.component.annotation.Atom;
import io.github.s3s3l.yggdrasil.boot.old.component.annotation.AutoInjected;
import io.github.s3s3l.yggdrasil.boot.old.component.annotation.Depot;
import io.github.s3s3l.yggdrasil.boot.old.component.annotation.Substantiality;

/**
 * ClassName:DepotTest <br>
 * Date: 2016年5月31日 下午5:41:48 <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@Depot
public class DepotTest {
	
	@AutoInjected
	private Student injectedStudent;

	@Atom("depotStudent")
	public Student getStudent() {
		Student student = new Student();
		student.setName("wow");
		return student;
	}

	@Atom("cloneStudent")
	public Student cloneStudent(@Substantiality("depotStudent") Student student){
		return student;
	}

	@Atom("injectedStudent")
	public Student injectedStudent(){
		return this.injectedStudent;
	}
}
