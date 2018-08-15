/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/******************************************************************
 * File:        AddDays.java
 * Created by:  Varun Ratnakar
 *****************************************************************/
package edu.isi.wings.ontapi.jena.extrules.date;

import java.util.Calendar;

import org.apache.jena.reasoner.rulesys.*;
import org.apache.jena.reasoner.rulesys.builtins.BaseBuiltin;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.datatypes.xsd.XSDDateTime;
import org.apache.jena.graph.*;

/**
 * Bind the third arg to the value of first arg to the power of the second arg.
 * arg3 = arg1 ^^ arg2
 */
public class AddDays extends BaseBuiltin {

	/**
	 * Return a name for this builtin, normally this will be the name of the
	 * functor that will be used to invoke it.
	 */
	public String getName() {
		return "add_days";
	}

	/**
	 * Return the expected number of arguments for this functor or 0 if the
	 * number is flexible.
	 */
	public int getArgLength() {
		return 3;
	}

	/**
	 * This method is invoked when the builtin is called in a rule body.
	 * 
	 * @param args
	 *            the array of argument values for the builtin, this is an array
	 *            of Nodes, some of which may be Node_RuleVariables.
	 * @param length
	 *            the length of the argument list, may be less than the length
	 *            of the args array for some rule engines
	 * @param context
	 *            an execution context giving access to other relevant data
	 * @return return true if the buildin predicate is deemed to have succeeded
	 *         in the current environment
	 */
	public boolean bodyCall(Node[] args, int length, RuleContext context) {
		checkArgs(length, context);
		BindingEnvironment env = context.getEnv();
		Node n1 = getArg(0, args, context);
		Node n2 = getArg(1, args, context);
		if (n1.isLiteral() && n2.isLiteral()) {
			Object v1 = n1.getLiteralValue();
			Object v2 = n2.getLiteralValue();
			Node sum = null;
			if (v1 instanceof XSDDateTime && v2 instanceof Integer) {
				XSDDateTime nv1 = (XSDDateTime) v1;
				Integer nv2 = (Integer) v2;
				Calendar cal = nv1.asCalendar();
				cal.add(Calendar.DATE, nv2);
				XSDDateTime nvsum = new XSDDateTime(cal);
				nvsum.narrowType(XSDDatatype.XSDdate);
				sum = NodeFactory.createLiteral(nvsum.toString(), null, XSDDatatype.XSDdate);
				return env.bind(args[2], sum);
			}
		}
		return false;
	}

}
