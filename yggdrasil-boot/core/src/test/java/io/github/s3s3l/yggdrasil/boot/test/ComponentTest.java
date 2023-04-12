package io.github.s3s3l.yggdrasil.boot.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.boot.old.component.ComponentScanner;
import io.github.s3s3l.yggdrasil.boot.old.component.annotation.AutoInjected;
import io.github.s3s3l.yggdrasil.boot.old.component.context.ComponentConfigurationContext;

/**
 * ClassName:CommonTest <br>
 * Date: 2016年5月5日 下午1:11:42 <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class ComponentTest {

	@AutoInjected
	public String name;
	public String value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Test
	public void componentTest() throws InstantiationException, IllegalAccessException {
		ComponentConfigurationContext ctx = ComponentScanner.scan("io.github.s3s3l.yggdrasil.boot.old.test");
		Student depotStudent = ctx.getComponent("depotStudent", Student.class);
		Assertions.assertEquals("wow", depotStudent.showName());
		Assertions.assertEquals("wow", depotStudent.getName());

		Student cloneStudent = ctx.getComponent("cloneStudent", Student.class);
		Assertions.assertEquals("wow", cloneStudent.showName());
		Assertions.assertEquals("wow", cloneStudent.getName());


		String newName = "haha";
		depotStudent.setName(newName);
		Assertions.assertEquals(newName, cloneStudent.showName());
		Assertions.assertEquals(newName, cloneStudent.getName());

		Student student3 = (Student) ctx.getComponent("depotStudent");
		Assertions.assertEquals(newName, student3.getName());

		Student student = ctx.getComponent("Student", Student.class);
		Assertions.assertNull(student.getName());

		Student injectedStudent = ctx.getAtom("injectedStudent", Student.class);
		Assertions.assertNull(injectedStudent.getName());

		School school = ctx.getFreshComponent(School.class);
		Assertions.assertEquals(school.getStudent(), depotStudent);;
	}
}
