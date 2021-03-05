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
import org.infai.ses.senergy.operators.FlexInput;
import org.infai.ses.senergy.operators.Message;
import org.infai.ses.senergy.util.DateParser;

public class ValueDiff extends BaseOperator {

    private Double previousValue;
    private final long minIntervalSeconds;
    private long intervalStartMillis;

    public ValueDiff() {
        super();
        minIntervalSeconds = Long.parseLong(config.getConfigValue("min_interval_seconds", "0"));
    }

    @Override
    public void run(Message message) {
        Double currentValue = 0.0;
        try {
            currentValue = message.getFlexInput("value").getValue();
        } catch (NoValueException e) {
            System.out.println(e.getMessage());
            System.out.println(message.getMessage().getMessages());
        }
        String timestamp = "";
        FlexInput timestampInput = message.getFlexInput("timestamp");
        long timestampMillis = 0;
        if (timestampInput != null) { // required for backwards compatibility
            try {
                timestamp = DateParser.parseDate(message.getFlexInput("timestamp").getString());
                timestampMillis = DateParser.parseDateMills(timestamp);
            } catch (NoValueException e) {
                // ignore
            }
        }
        if (previousValue == null) {
            intervalStartMillis = timestampMillis;
            previousValue = currentValue;
            return;
        }
        if (timestampMillis < intervalStartMillis + (minIntervalSeconds * 1000)) {
            // interval not complete
            return;
        }
        double diff = currentValue - previousValue;
        intervalStartMillis = timestampMillis;
        previousValue = currentValue;
        message.output("diff", (Math.round(diff * 1000.0) / 1000.0));
        message.output("timestamp", timestamp);
    }

    @Override
    public Message configMessage(Message message) {
        message.addFlexInput("value");
        message.addFlexInput("timestamp");
        return message;
    }
}
