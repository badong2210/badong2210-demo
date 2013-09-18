package org.badong2210.core.dozer;

import org.dozer.Mapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.Assert.assertEquals;

/**
 * Test for {@link EnumStringBiDirectionalConverter} class.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-dozer.xml" })
public class EnumStringBiDirectionalConverterTest {

    @Autowired
    private Mapper dozer;

    @Test
    public void testStringToEnum() throws Exception {
        Bean1 bean1 = new Bean1();
        bean1.setSampleEnum(SampleEnum.TEST1.name());
        Bean2 bean2 = dozer.map(bean1, Bean2.class);
        assertEquals(bean2.getSampleEnum(), SampleEnum.TEST1);
    }

    @Test
    public void testEnumToString() throws Exception {
        Bean2 bean2 = new Bean2();
        bean2.setSampleEnum(SampleEnum.TEST2);

        Bean1 bean1 = dozer.map(bean2, Bean1.class);
        assertEquals(SampleEnum.TEST2.name(), bean1.getSampleEnum());
    }

    public static enum SampleEnum {
        TEST1, TEST2
    }

    public static class Bean1 {
        private String sampleEnum;

        public String getSampleEnum() {
            return sampleEnum;
        }

        public void setSampleEnum(String sampleEnum) {
            this.sampleEnum = sampleEnum;
        }
    }

    public static class Bean2 {
        private SampleEnum sampleEnum;

        public SampleEnum getSampleEnum() {
            return sampleEnum;
        }

        public void setSampleEnum(SampleEnum sampleEnum) {
            this.sampleEnum = sampleEnum;
        }
    }
}
