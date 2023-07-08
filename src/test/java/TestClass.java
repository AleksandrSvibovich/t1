import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.FileReader;
import java.io.IOException;

public class TestClass {

    JSONObject json;


    @BeforeSuite
    public void parseJSON() {
        Object object;
        try {
            object = new JSONParser().parse(new FileReader("src/test/result.json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        json = (JSONObject) object;
    }

    @Test()
    @Parameters("positive")
    public void checkDetectivesArraySizeAndMainIDs() {
        boolean sizeCheck = false;
        Long mainID = null;
        JSONArray detectives = (JSONArray) json.get("detectives");
        int detectiveArraySize = detectives.size();
        if (detectiveArraySize >= 1 && detectiveArraySize <= 3) {
            sizeCheck = true;
            for (Object detective : detectives) {
                JSONObject currentDetective = (JSONObject) detective;
                mainID = (Long) currentDetective.get("MainId");
            }
        }
        Assert.assertTrue(sizeCheck && mainID >= 0 && mainID <= 10,
                "MainId available values in range from 0 to 10. Detectives available values in range from 1 to 3. ArraySize " + detectiveArraySize);
    }

    @Test()
    @Parameters("positive")
    public void checkDetectivesCategoryIDValues() {
        JSONArray detectives = (JSONArray) json.get("detectives");
        Long catID = null;
        for (Object detective : detectives) {
            JSONObject currentDetective = (JSONObject) detective;
            JSONArray categories = (JSONArray) currentDetective.get("categories");
            for (Object category : categories) {
                JSONObject test = (JSONObject) category;
                catID = (Long) test.get("CategoryID");

            }
        }
        Assert.assertTrue(catID >= 1 && catID <= 2, "Category ID is not correct, current value is - " + catID);
    }

    @Test()
    @Parameters("positive")
    public void checkNullValueOfParameterExtraForCategoryTwo() {
        JSONArray detectives = (JSONArray) json.get("detectives");
        Object extra = null;
        for (Object detective : detectives) {
            JSONObject currentDetective = (JSONObject) detective;
            JSONArray categories = (JSONArray) currentDetective.get("categories");
            for (Object category : categories) {
                JSONObject test = (JSONObject) category;
                Long catID = (Long) test.get("CategoryID");
                if (catID != 2) {
                    break;
                }
                extra = ((JSONObject) category).get("extra");

            }
        }
        Assert.assertNull(extra, "Extra is not null " + extra);
    }

    @Test()
    @Parameters("positive")
    public void checkSizeOfExtraArrayForCategoryOne() {
        int extraArrSize = 0;
        JSONArray detectives = (JSONArray) json.get("detectives");
        for (Object detective : detectives) {
            JSONObject currentDetective = (JSONObject) detective;
            JSONArray categories = (JSONArray) currentDetective.get("categories");
            for (Object category : categories) {
                JSONObject test = (JSONObject) category;
                Long catID = (Long) test.get("CategoryID");
                if (catID != 1) {
                    break;
                }
                Object extra = ((JSONObject) category).get("extra");
                JSONArray extraArray = (JSONArray) ((JSONObject) extra).get("extraArray");
                extraArrSize = extraArray.size();

            }
        }
        Assert.assertTrue(extraArrSize >= 1, " extraArray for CategoryID = 1 should have size more than 1");
    }

    @Test()
    @Parameters("positive")
    public void checkSuccessValue() {
        boolean jsonContainsSherlock = false;
        JSONArray detectives = (JSONArray) json.get("detectives");
        for (Object detective : detectives) {
            JSONObject currentDetective = (JSONObject) detective;
            String name = (String) currentDetective.get("firstName");
            if (name.equalsIgnoreCase("Sherlock")) {
                jsonContainsSherlock = true;
            }
        }
        boolean successFlag = (boolean) json.get("success");
        Assert.assertEquals(successFlag, jsonContainsSherlock,
                "Parameter success can by true only in case detectives array contains detective with firstName Sherlock. " + "Value of success "
                        + successFlag + ". Detectives array contains Sherlock " + jsonContainsSherlock);
    }

    //____________________________________

    @Test()
    @Parameters("negative")
    public void checkIncorrectDetectivesArraySize() {
        JSONArray detectives = (JSONArray) json.get("detectives");
        int detectiveArraySize = detectives.size();
        Assert.assertFalse(detectiveArraySize < 1 || detectiveArraySize > 3, "Detectives array size - " + detectives.size());
    }

    @Test()
    @Parameters("negative")
    public void checkIncorrectDetectivesMainIDValues() {
        JSONArray detectives = (JSONArray) json.get("detectives");
        for (Object detective : detectives) {
            JSONObject currentDetective = (JSONObject) detective;
            Long mainID = (Long) currentDetective.get("MainId");
            Assert.assertFalse(mainID < 0 || mainID > 10,
                    "MainId value for " + detective + ", is = " + currentDetective.get("MainId"));

        }
    }


    @Test()
    @Parameters("negative")
    public void checkIncorrectDetectivesCategoryIDValues() {
        JSONArray detectives = (JSONArray) json.get("detectives");
        for (Object detective : detectives) {
            JSONObject currentDetective = (JSONObject) detective;
            JSONArray categories = (JSONArray) currentDetective.get("categories");
            for (Object category : categories) {
                JSONObject test = (JSONObject) category;
                Long catID = (Long) test.get("CategoryID");
                Assert.assertFalse(catID < 1 || catID > 2, "Category ID is correct, current value is - " + catID);
            }
        }
    }

    @Test()
    @Parameters("negative")
    public void checkNullValueOfParameterExtraExcludeCategoryTwo() {
        JSONArray detectives = (JSONArray) json.get("detectives");
        for (Object detective : detectives) {
            JSONObject currentDetective = (JSONObject) detective;
            JSONArray categories = (JSONArray) currentDetective.get("categories");
            for (Object category : categories) {
                JSONObject test = (JSONObject) category;
                Long catID = (Long) test.get("CategoryID");
                Object extra;
                if (catID == 2) {
                    break;
                }
                extra = ((JSONObject) category).get("extra");
                Assert.assertNotNull(extra, "Extra is not null " + extra);
            }
        }
    }

    @Test()
    @Parameters("negative")
    public void checkIncorrectSizeOfExtraArrayForCategoryOne() {
        JSONArray detectives = (JSONArray) json.get("detectives");
        for (Object detective : detectives) {
            JSONObject currentDetective = (JSONObject) detective;
            JSONArray categories = (JSONArray) currentDetective.get("categories");
            for (Object category : categories) {
                JSONObject test = (JSONObject) category;
                Long catID = (Long) test.get("CategoryID");
                if (catID != 1) {
                    break;
                }
                Object extra = ((JSONObject) category).get("extra");
                JSONArray extraArray = (JSONArray) ((JSONObject) extra).get("extraArray");
                Assert.assertNull(extraArray, "Extra array not exist for category One");
            }
        }
    }

    @Test()
    @Parameters("negative")
    public void checkUnSuccessValue() {
        boolean jsonContainsSherlock = false;
        JSONArray detectives = (JSONArray) json.get("detectives");
        for (Object detective : detectives) {
            JSONObject currentDetective = (JSONObject) detective;
            String name = (String) currentDetective.get("firstName");
            if (name.equalsIgnoreCase("Sherlock")) {
                jsonContainsSherlock = true;
            }
        }
        boolean successFlag = (boolean) json.get("success");
        Assert.assertEquals(successFlag, jsonContainsSherlock,
                "Parameter success can by true only in case detectives array contains detective with firstName Sherlock. " + "Value of success "
                        + successFlag + ". Detectives array contains Sherlock " + jsonContainsSherlock);
    }
}


