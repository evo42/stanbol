/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.stanbol.enhancer.it;

import org.junit.Test;

/** Simple test of the enhancer homepage, mostly
 *  to show how simple a test can be */
public class HomepageTest extends StanbolTestBase {
    @Test
    public void testHomepageHasCss() throws Exception {
        executor.execute(builder.buildRequest("/"))
        .assertStatus(200)
        .assertContentContains(
            "/static/style/stanbol.css", 
            "The RESTful Semantic Engine");
    }
}