package model.config;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;

import static org.junit.Assert.*;

public class ConfigReaderTest {

    private final String BASE_TEST_FILE= "src/main/resources/test_cfg/test_base.cfg";
    private final String OVERRIDE_TEST_FILE= "src/main/resources/test_cfg/test_override.cfg";

    private ConfigReader testedReader;


    @Test
    public void getBooleanValue() throws Exception {
        setDefaultTestReader();

        assertEquals(false, testedReader.getBooleanValue("BASE_2","TEST_BOOL_1"));
        assertEquals(true, testedReader.getBooleanValue("BASE_2","TEST_BOOL_2"));
        assertEquals(true,testedReader.getBooleanValue("BASE_2","TEST_BOOL_TO_OVERRIDE_1"));
        assertEquals(false,testedReader.getBooleanValue("BASE_2","TEST_BOOL_TO_OVERRIDE_2"));
    }

    @Test(expected = IllegalStateException.class)
    public void getBooleanValue_tag_not_found() throws Exception {
        setDefaultTestReader();
        testedReader.getBooleanValue("NOT_EXISTING_TAG","hue");
    }

    @Test(expected = IllegalStateException.class)
    public void getBooleanValue_value_not_found() throws Exception {
        setDefaultTestReader();
        testedReader.getBooleanValue("BASE_2","NON_EXISTING_VARIABLE");
    }

    @Test
    public void getBooleanValue_default() throws Exception {
        setDefaultTestReader();

        assertEquals(false, testedReader.getBooleanValue("BASE_2","TEST_BOOL_1",true));

        assertEquals(true,testedReader.getBooleanValue("BASE_2","NOT_EXISTING",true));
        assertEquals(false,testedReader.getBooleanValue("BASE_2","NOT_EXISTING",false));
        
        assertEquals(true,testedReader.getBooleanValue("NOTEXISTING","NOT_EXISTING",true));
        assertEquals(false,testedReader.getBooleanValue("NOTEXISTING","NOT_EXISTING",false));
    }

    @Test
    public void overrideTestBoolean() throws Exception
    {
        setCustomConfigReader(BASE_TEST_FILE,null);
        assertEquals(false,testedReader.getBooleanValue("BASE_2","TEST_BOOL_TO_OVERRIDE_1"));
        setDefaultTestReader();
        assertEquals(true,testedReader.getBooleanValue("BASE_2","TEST_BOOL_TO_OVERRIDE_1"));
    }
    

    @Test
    public void getIntValue() throws Exception {
        setDefaultTestReader();

        assertEquals(0, testedReader.getIntValue("BASE","TEST_INT_1"));
        assertEquals(-2, testedReader.getIntValue("BASE","TEST_INT_2"));
        assertEquals(4, testedReader.getIntValue("BASE","TEST_INT_3"));
        assertEquals(7,testedReader.getIntValue("BASE","TEST_INT_TO_OVERRIDE_1"));
        assertEquals(-8,testedReader.getIntValue("BASE","TEST_INT_TO_OVERRIDE_2"));
        assertEquals(9,testedReader.getIntValue("BASE","TEST_INT_TO_OVERRIDE_3"));
        
    }

    @Test
    public void getIntValue_defaut() throws Exception {
        setDefaultTestReader();

        assertEquals(0, testedReader.getIntValue("BASE","TEST_INT_1",0));
        assertEquals(0, testedReader.getIntValue("BASE","TEST_INT_1",1));

        assertEquals(6, testedReader.getIntValue("BASE","NOT_EXISTING",6));
        assertEquals(7, testedReader.getIntValue("NOT_EXISTING","TEST_INT_1",7));

    }

    @Test
    public void overrideTestInt() throws Exception
    {
        setCustomConfigReader(BASE_TEST_FILE,null);

        assertEquals(0,testedReader.getIntValue("BASE","TEST_INT_TO_OVERRIDE_1"));
        assertEquals(-2,testedReader.getIntValue("BASE","TEST_INT_TO_OVERRIDE_2"));
        assertEquals(4,testedReader.getIntValue("BASE","TEST_INT_TO_OVERRIDE_3"));

        setDefaultTestReader();

        assertEquals(7,testedReader.getIntValue("BASE","TEST_INT_TO_OVERRIDE_1"));
        assertEquals(-8,testedReader.getIntValue("BASE","TEST_INT_TO_OVERRIDE_2"));
        assertEquals(9,testedReader.getIntValue("BASE","TEST_INT_TO_OVERRIDE_3"));

    }


    private void setDefaultTestReader()throws Exception
    {
        Constructor<ConfigReader>  constructor =  ConfigReader.class.getDeclaredConstructor(String.class,String.class);
        constructor.setAccessible(true);

        testedReader = constructor.newInstance(BASE_TEST_FILE
                ,OVERRIDE_TEST_FILE);
    }

    private ConfigReader getConfigReader(String base, String override) throws  Exception
    {
        Constructor<ConfigReader>  constructor =  ConfigReader.class.getDeclaredConstructor(String.class,String.class);
        constructor.setAccessible(true);

        return constructor.newInstance(base
                ,override);
    }

    private void setCustomConfigReader(String base,String override) throws  Exception
    {
        testedReader=getConfigReader(base,override);
    }
}