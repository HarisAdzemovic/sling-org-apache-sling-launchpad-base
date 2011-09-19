/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.sling.launchpad.app;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.launchpad.base.shared.SharedConstants;

import junit.framework.TestCase;

public class MainTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("___sling_dont_exit___", "true");
    }

    public void test_parseCommandLine_null_args() {
        String[] args = null;
        Map<String, String> commandline = Main.parseCommandLine(args);
        assertNotNull("commandline map must not be null", commandline);
        assertTrue("commandline map must be empty", commandline.isEmpty());
    }

    public void test_parseCommandLine_empty_args() {
        String[] args = {};
        Map<String, String> commandline = Main.parseCommandLine(args);
        assertNotNull("commandline map must not be null", commandline);
        assertTrue("commandline map must be empty", commandline.isEmpty());
    }

    public void test_parseCommandLine_single_dash() {
        String[] args = { "-" };
        Map<String, String> commandline = Main.parseCommandLine(args);
        assertNotNull("commandline map must not be null", commandline);
        assertTrue("commandline map must be empty", commandline.isEmpty());
    }

    public void test_parseCommandLine_single_arg_no_par() {
        String[] args = { "-a" };
        Map<String, String> commandline = Main.parseCommandLine(args);
        assertNotNull("commandline map must not be null", commandline);
        assertEquals("commandline map must have one entry", 1,
            commandline.size());
        assertEquals("single argument must be " + args[0].charAt(1),
            String.valueOf(args[0].charAt(1)),
            commandline.keySet().iterator().next());
        assertEquals("single argument value must be " + args[0].charAt(1),
            String.valueOf(args[0].charAt(1)),
            commandline.values().iterator().next());
    }

    public void test_parseCommandLine_single_arg_with_par() {
        String[] args = { "-a", "value" };
        Map<String, String> commandline = Main.parseCommandLine(args);
        assertNotNull("commandline map must not be null", commandline);
        assertEquals("commandline map must have one entry", 1,
            commandline.size());
        assertEquals("single argument must be " + args[0].charAt(1),
            String.valueOf(args[0].charAt(1)),
            commandline.keySet().iterator().next());
        assertEquals("single argument value must be " + args[1], args[1],
            commandline.values().iterator().next());
    }

    public void test_parseCommandLine_two_args_no_par() {
        String[] args = { "-a", "-b" };
        Map<String, String> commandline = Main.parseCommandLine(args);
        assertNotNull("commandline map must not be null", commandline);
        assertEquals("commandline map must have two entries", 2,
            commandline.size());
        assertEquals("argument a must a", "a", commandline.get("a"));
        assertEquals("argument b must b", "b", commandline.get("b"));
    }

    public void test_parseCommandLine_two_args_first_par() {
        String[] args = { "-a", "apar", "-b" };
        Map<String, String> commandline = Main.parseCommandLine(args);
        assertNotNull("commandline map must not be null", commandline);
        assertEquals("commandline map must have two entries", 2,
            commandline.size());
        assertEquals("argument a must apar", "apar", commandline.get("a"));
        assertEquals("argument b must b", "b", commandline.get("b"));
    }

    public void test_parseCommandLine_two_args_second_par() {
        String[] args = { "-a", "-b", "bpar" };
        Map<String, String> commandline = Main.parseCommandLine(args);
        assertNotNull("commandline map must not be null", commandline);
        assertEquals("commandline map must have two entries", 2,
            commandline.size());
        assertEquals("argument a must a", "a", commandline.get("a"));
        assertEquals("argument b must bpar", "bpar", commandline.get("b"));
    }

    public void test_parseCommandLine_two_args_all_par() {
        String[] args = { "-a", "apar", "-b", "bpar" };
        Map<String, String> commandline = Main.parseCommandLine(args);
        assertNotNull("commandline map must not be null", commandline);
        assertEquals("commandline map must have two entries", 2,
            commandline.size());
        assertEquals("argument a must apar", "apar", commandline.get("a"));
        assertEquals("argument b must bpar", "bpar", commandline.get("b"));
    }

    public void test_parseCommandLine_three_args_with_dash() {
        String[] args = { "-a", "apar", "-", "-b", "bpar" };
        Map<String, String> commandline = Main.parseCommandLine(args);
        assertNotNull("commandline map must not be null", commandline);
        assertEquals("commandline map must have three entries", 3,
            commandline.size());
        assertEquals("argument a must apar", "apar", commandline.get("a"));
        assertEquals("argument -b must -b", "-b", commandline.get("-b"));
        assertEquals("argument bpar must bpar", "bpar", commandline.get("bpar"));
    }

    public void test_parseCommandLine_single_arg_with_dash_par() {
        String[] args = { "-a", "-" };
        Map<String, String> commandline = Main.parseCommandLine(args);
        assertNotNull("commandline map must not be null", commandline);
        assertEquals("commandline map must have three entries", 1,
            commandline.size());
        assertEquals("argument a must -", "-", commandline.get("a"));
    }

    public void test_convertCommandLineArgs_no_args() {
        Map<String, String> props = Main.convertCommandLineArgs(new HashMap<String, String>());
        assertNotNull(props);
        assertTrue(props.isEmpty());
    }

    public void test_converCommandLineArgs_unknown() {
        assertNull(Main.convertCommandLineArgs(new HashMap<String, String>() {
            {
                put("u", "short");
            }
        }));
        assertNull(Main.convertCommandLineArgs(new HashMap<String, String>() {
            {
                put("longer", "long");
            }
        }));
    }

    public void test_converCommandLineArgs_j_start_stop_status() {
        Map<String, String> props = Main.convertCommandLineArgs(new HashMap<String, String>() {
            {
                put("j", "j");
                put("start", "start");
                put("stop", "stop");
                put("status", "status");
            }
        });
        assertNotNull(props);
        assertEquals(4, props.size());
        assertEquals("j", props.get("j"));
        assertEquals("start", props.get("start"));
        assertEquals("stop", props.get("stop"));
        assertEquals("status", props.get("status"));
    }

    public void test_converCommandLineArgs_l() {
        Map<String, String> props = Main.convertCommandLineArgs(new HashMap<String, String>() {
            {
                put("l", "INFO");
            }
        });
        assertNotNull(props);
        assertEquals(1, props.size());
        assertEquals("INFO", props.get("org.apache.sling.commons.log.level"));

        Map<String, String> props2 = Main.convertCommandLineArgs(new HashMap<String, String>() {
            {
                final String l = "l";
                put(l, l);
            }
        });
        assertNull(props2);
    }

    public void test_converCommandLineArgs_f() {
        Map<String, String> props = Main.convertCommandLineArgs(new HashMap<String, String>() {
            {
                put("f", "sling.changed");
            }
        });
        assertNotNull(props);
        assertEquals(1, props.size());
        assertEquals("sling.changed", props.get("org.apache.sling.commons.log.file"));

        Map<String, String> props1 = Main.convertCommandLineArgs(new HashMap<String, String>() {
            {
                put("f", "-");
            }
        });
        assertNotNull(props1);
        assertEquals(1, props1.size());
        assertEquals("", props1.get("org.apache.sling.commons.log.file"));

        Map<String, String> props2 = Main.convertCommandLineArgs(new HashMap<String, String>() {
            {
                final String f = "f";
                put(f, f);
            }
        });
        assertNull(props2);
    }

    public void test_converCommandLineArgs_c() {
        Map<String, String> props = Main.convertCommandLineArgs(new HashMap<String, String>() {
            {
                put("c", "sling.changed");
            }
        });
        assertNotNull(props);
        assertEquals(1, props.size());
        assertEquals("sling.changed", props.get(SharedConstants.SLING_HOME));

        Map<String, String> props2 = Main.convertCommandLineArgs(new HashMap<String, String>() {
            {
                final String c = "c";
                put(c, c);
            }
        });
        assertNull(props2);
    }

    public void test_converCommandLineArgs_a() {
        Map<String, String> props = Main.convertCommandLineArgs(new HashMap<String, String>() {
            {
                put("a", "0.0.0.0");
            }
        });
        assertNotNull(props);
        assertEquals(0, props.size());
    }

    public void test_converCommandLineArgs_p() {
        Map<String, String> props = Main.convertCommandLineArgs(new HashMap<String, String>() {
            {
                put("p", "1234");
            }
        });
        assertNotNull(props);
        assertEquals(1, props.size());
        assertEquals("1234", props.get("org.osgi.service.http.port"));

        Map<String, String> props1 = Main.convertCommandLineArgs(new HashMap<String, String>() {
            {
                put("p", "abc");
            }
        });
        assertNull(props1);

        Map<String, String> props2 = Main.convertCommandLineArgs(new HashMap<String, String>() {
            {
                final String p = "p";
                put(p, p);
            }
        });
        assertNull(props2);
    }
}
