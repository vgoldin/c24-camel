/**
 * Copyright (C) FuseSource, Inc.
 * http://fusesource.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package biz.c24.io.camel.c24io;

import biz.c24.io.api.presentation.JsonSinkv2;
import biz.c24.io.api.presentation.TextualSource;
import biz.c24.io.gettingstarted.transaction.Transactions;
import biz.c24.io.gettingstarted.transaction.TransactionsElement;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import java.util.List;

import static biz.c24.io.camel.c24io.CamelC24IO.c24io;

/**
 * @version $Revision$
 */
public class ReformatTest2 extends CamelTestSupport {
    @Test
    public void testC24() throws Exception {
        MockEndpoint resultEndpoint = resolveMandatoryEndpoint("mock:result", MockEndpoint.class);
        resultEndpoint.expectedMessageCount(1);

        resultEndpoint.assertIsSatisfied();

        List<Exchange> list = resultEndpoint.getReceivedExchanges();
        Exchange exchange = list.get(0);
        Message in = exchange.getIn();

        String text = in.getBody(String.class);
        log.info("Received: " + text);
    }

    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {
                JsonSinkv2 jsonSinkv2 = new JsonSinkv2();
                jsonSinkv2.setAlwaysOutputMandatoryFields(true);
                C24IOFormat c24IoFormat = new C24IOFormat(TransactionsElement.getInstance(), new TextualSource(), jsonSinkv2);
                from("file:src/test/data?noop=true").
                        unmarshal(CamelC24IO.c24io(Transactions.class)).
                        marshal(c24IoFormat).
                                to("mock:result");
            }
        };
    }
}
