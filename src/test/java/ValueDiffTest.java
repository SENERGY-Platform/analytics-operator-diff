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

import org.infai.ses.senergy.models.DeviceMessageModel;
import org.infai.ses.senergy.models.MessageModel;
import org.infai.ses.senergy.operators.Config;
import org.infai.ses.senergy.operators.Helper;
import org.infai.ses.senergy.operators.Message;
import org.infai.ses.senergy.testing.utils.JSONHelper;
import org.infai.ses.senergy.utils.ConfigProvider;
import org.junit.Test;
import org.junit.Assert;


public class ValueDiffTest {

    private ValueDiff testOperator;
    private final org.json.simple.JSONArray messages = new JSONHelper().parseFile("messages.json");
    private final String configString = new JSONHelper().parseFile("config.json").toString();

    @Test
    public void testRun(){
        Config config = new Config(configString);
        String topicName = config.getInputTopicsConfigs().get(0).getName();
        ConfigProvider.setConfig(config);
        Message message = new Message();
        MessageModel model =  new MessageModel();
        testOperator = new ValueDiff();
        testOperator.configMessage(message);
        int index = 0;
        Double expected []  = new Double []{0.0, 5.0};
        for(Object msg : messages){
            DeviceMessageModel deviceMessageModel = JSONHelper.getObjectFromJSONString(msg.toString(), DeviceMessageModel.class);
            assert deviceMessageModel != null;
            model.putMessage(topicName, Helper.deviceToInputMessageModel(deviceMessageModel, topicName));
            message.setMessage(model);
            testOperator.run(message);
            Assert.assertEquals(expected[index++], message.getMessage().getOutputMessage().getAnalytics().get("diff"));
        }
    }
}
