/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package org.sog.cli;

import joptsimple.OptionDescriptor;

import java.util.HashSet;
import java.util.Map;

/**
 * <p>
 * .
 * </p>
 *
 * @author sagyf yang
 * @version 1.0 2014-02-10 14:44
 * @since JDK 1.6
 */
public class CliHelpFormatter implements joptsimple.HelpFormatter {
    public String format(Map<String, ? extends OptionDescriptor> options) {
        StringBuilder buffer = new StringBuilder();
        for (OptionDescriptor each : new HashSet<OptionDescriptor>(options.values())) {
            buffer.append(lineFor(each));
        }
        return buffer.toString();
    }

    private String lineFor(OptionDescriptor descriptor) {
        if (descriptor.representsNonOptions()) {
            return descriptor.argumentDescription() + '(' + descriptor.argumentTypeIndicator() + "): "
                    + descriptor.description() + System.getProperty("line.separator");
        }

        StringBuilder line = new StringBuilder(descriptor.options().toString());
        line.append(": description = ").append(descriptor.description());
        line.append(System.getProperty("line.separator"));
        return line.toString();
    }
}
