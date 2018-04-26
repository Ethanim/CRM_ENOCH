package test;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.encrm.system.entity.Menu;
import com.encrm.system.service.IMenuService;


public class MenuTest {
	
	@Test
	public void test1(){
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		IMenuService service = ac.getBean(IMenuService.class);
		List<Menu> list = service.queryAllMenu();
		System.out.println(list.get(0).getMenuname());
		System.out.println(list.get(0).getMenuname());
	}
}
