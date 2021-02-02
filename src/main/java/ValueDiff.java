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


import org.infai.ses.senergy.exceptions.NoValueException;
import org.infai.ses.senergy.operators.BaseOperator;
import org.infai.ses.senergy.operators.Message;

public class ValueDiff extends BaseOperator {

    private Double previousValue;

    @Override
    public void run(Message message) {
        Double currentValue = 0.0;
        try {
            currentValue = message.getFlexInput("value").getValue();
        } catch (NoValueException e) {
            System.out.println(e.getMessage());
            System.out.println(message.getMessage().getMessages());
        }
        Double diff;
        if (previousValue != null) {
            diff = currentValue - previousValue;
        } else {
            diff = 0.0;
        }
        previousValue = currentValue;
        message.output("diff", (Math.round(diff * 1000.0) / 1000.0));
    }

    @Override
    public Message configMessage(Message message) {
        message.addFlexInput("value");
        return message;
    }
}
