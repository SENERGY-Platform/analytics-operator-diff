/*
 * Copyright 2018 InfAI (CC SES)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.jayway.jsonpath.JsonPath;
import org.infai.seits.sepl.operators.Builder;
import org.infai.seits.sepl.operators.Message;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.Assert;


public class ValueDiffTest {

    static ValueDiff valueDiff;
    protected JSONObject ob1 = new JSONObject();
    protected JSONObject ob2 = new JSONObject();

    public ValueDiffTest (){
        ob1.put("device_id", "1").put("value", new JSONObject().put("reading", new JSONObject().put("value", new Double(5))));
        ob2.put("device_id", "1").put("value", new JSONObject().put("reading", new JSONObject().put("value", new Double(10))));
    }

    @Test
    public void testRun(){
        valueDiff  = new ValueDiff();
        Builder builder = new Builder("1", "1");
        JSONArray config = new JSONArray().put(new JSONObject().put("Name","test")
                .put("FilterType", "DeviceId")
                .put("FilterValue", "1")
                .put("Mappings", new JSONArray().put(
                        new JSONObject().put("Source", "value.reading.value").put("Dest","value"))
                ));
        Message message = new Message(builder.formatMessage(ob1.toString()));
        message.setConfig(config.toString());
        valueDiff.config(message);
        valueDiff.run(message);
        message.setMessage(builder.formatMessage(ob2.toString()));
        valueDiff.run(message);
        Assert.assertEquals(new Double(5.0), JsonPath.parse(message.getMessageString()).read("$.analytics.diff"));
    }
}
