/**
 * CodeBucket, a code snippet repository
 * Copyright (c) 2012, Paulo Roberto Massa Cereda
 * All rights reserved.
 * 
 * This project is part of my CodeBucket repository.
 *
 * Redistribution and  use in source  and binary forms, with  or without
 * modification, are  permitted provided  that the  following conditions
 * are met:
 *
 * 1. Redistributions  of source  code must  retain the  above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form  must reproduce the above copyright
 * notice, this list  of conditions and the following  disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither  the name  of the  project's author nor  the names  of its
 * contributors may be used to  endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS  PROVIDED BY THE COPYRIGHT  HOLDERS AND CONTRIBUTORS
 * "AS IS"  AND ANY  EXPRESS OR IMPLIED  WARRANTIES, INCLUDING,  BUT NOT
 * LIMITED  TO, THE  IMPLIED WARRANTIES  OF MERCHANTABILITY  AND FITNESS
 * FOR  A PARTICULAR  PURPOSE  ARE  DISCLAIMED. IN  NO  EVENT SHALL  THE
 * COPYRIGHT HOLDER OR CONTRIBUTORS BE  LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY,  OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT  NOT LIMITED  TO, PROCUREMENT  OF SUBSTITUTE  GOODS OR  SERVICES;
 * LOSS  OF USE,  DATA, OR  PROFITS; OR  BUSINESS INTERRUPTION)  HOWEVER
 * CAUSED AND  ON ANY THEORY  OF LIABILITY, WHETHER IN  CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY  OUT  OF  THE USE  OF  THIS  SOFTWARE,  EVEN  IF ADVISED  OF  THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

// package definition
package com.github.cereda.codebucket.interviewformatter;

// needed imports
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

/**
 * The main window.
 * 
 * @author Paulo Roberto Massa Cereda
 * @version 1.0
 * @since 1.0
 */
public class Formatter extends JFrame {

    // both panels, input and output
    private JPanel inputPanel;
    private JPanel outputPanel;
    
    // two areas, input and output
    private RSyntaxTextArea inputArea;
    private RSyntaxTextArea outputArea;
    
    // two buttons, convert and copy
    private JButton convertButton;
    private JButton copyButton;
    
    // flag to set the logical markup
    // which determines the author's name.
    private final String MARKUP = "strong";

    /**
     * Constructor.
     */
    public Formatter() {

        // window settings
        setTitle("Interview Formatter");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new MigLayout());

        // set panel configs
        inputPanel = new JPanel(new MigLayout());
        outputPanel = new JPanel(new MigLayout());

        // create two areas
        inputArea = new RSyntaxTextArea();
        outputArea = new RSyntaxTextArea();

        // define their settings
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        inputArea.setAntiAliasingEnabled(true);
        inputArea.setCodeFoldingEnabled(false);
        inputArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_HTML);
        
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setAntiAliasingEnabled(true);
        outputArea.setCodeFoldingEnabled(false);
        outputArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_HTML);

        // add them to a scroll pane
        RTextScrollPane scrollInput = new RTextScrollPane(inputArea);
        RTextScrollPane scrollOutput = new RTextScrollPane(outputArea);

        // create two buttons
        convertButton = new JButton("Convert");
        copyButton = new JButton("Copy to clipboard");

        // add the action to the convert button
        convertButton.addActionListener(new ActionListener() {

            // create a new action
            public void actionPerformed(ActionEvent e) {

                // fallback, if there's nothing to convert
                if (inputArea.getText().trim().isEmpty()) {
                    
                    // simply return
                    return;
                }

                // flag to indicate a previous block
                boolean flag = false;
                
                // get all lines separately
                String[] lines = inputArea.getText().split("\\n");

                // clear output area and add
                // first HTML entry
                outputArea.setText("");
                outputArea.append("<dl>\n\n");

                // iterate through lines
                for (String line : lines) {

                    // replace the paragraph marks
                    line = line.replaceAll("\\<p>", "");
                    line = line.replaceAll("\\</p>", "");

                    // if the line starts with the markup, it's
                    // another person talking
                    if (line.trim().startsWith("<" + MARKUP + ">")) {
                        
                        // get the positions of the person's name
                        int i = line.indexOf("<" + MARKUP + ">") + ("<" + MARKUP + ">").length();
                        int j = line.indexOf("</" + MARKUP + ">") - 1;

                        // if there was a previous block
                        if (flag) {
                            
                            // close previous block
                            outputArea.append("</dd>\n\n");
                        }

                        // it's another person talking,
                        // so set the flag to true
                        flag = true;
                        
                        // append the person's name
                        outputArea.append("<dt>" + line.substring(i, j) + "</dt>\n<dd>\n");
                        
                        // and add the first line without
                        // the person's name
                        outputArea.append("<p>" + line.substring(j + ("<" + MARKUP + ">").length() + 2).trim() + "</p>\n");
                        
                    } else {
                        
                        // if it's not an empty line
                        if (!line.trim().isEmpty()) {
                            
                            // simply add the line without any changes
                            outputArea.append("<p>" + line.trim() + "</p>\n");
                        }
                    }
                }
                
                // add the final block
                outputArea.append("</dd>\n\n");
                outputArea.append("</dl>\n");
                
                // show message
                JOptionPane.showMessageDialog(null, "The interview was successfully formatted.", "Interview Formatter", JOptionPane.INFORMATION_MESSAGE);

            }
        });

        // add the action to the copy button
        copyButton.addActionListener(new ActionListener() {

            // create a new action
            public void actionPerformed(ActionEvent e) {
                
                // get the text to be copied
                StringSelection stringSelection = new StringSelection(outputArea.getText());
                
                // create a new clipboard instance
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                
                // set its contents
                clipboard.setContents(stringSelection, null);
                
                // show message
                JOptionPane.showMessageDialog(null, "The interview was successfully copied to the clipboard.", "Interview Formatter", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // add components to the panel
        inputPanel.add(scrollInput, "width 400, height 200, wrap");
        inputPanel.add(convertButton, "growx");
        
        outputPanel.add(scrollOutput, "width 400, height 200, wrap");
        outputPanel.add(copyButton, "growx");
        
        // add panels to the window
        add(inputPanel, "wrap");
        add(outputPanel);

        // pack layout
        pack();
        
        // center the window
        setLocationRelativeTo(null);

    }
}
