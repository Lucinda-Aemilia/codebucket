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
package com.github.cereda.codebucket.codeprinter;

// needed imports
import java.io.File;
import java.io.FileWriter;
import java.util.Collection;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

/**
 * The generator class.
 * @author Paulo Roberto Massa Cereda
 */
public class Generator {

    /**
     * Runs the generator.
     */
    public void run() {

        // get the title
        String title = JOptionPane.showInputDialog("Document title:", "");

        // check if it's valid
        if ((title == null) || (title.trim().isEmpty())) {

            // nope, it's not
            JOptionPane.showMessageDialog(null, "No title was given, bye.", "Uh-oh!", JOptionPane.WARNING_MESSAGE);

        } else {
            
            // directory chooser
            JFileChooser dirChooser = new JFileChooser();
            
            // set title
            dirChooser.setDialogTitle("Choose the root directory of your project.");
            
            // set file selection mode
            dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            
            // disable filter
            dirChooser.setAcceptAllFileFilterUsed(false);
            
            // show it
            if (dirChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                
                // filter all .java files
                IOFileFilter javaFilter = FileFilterUtils.suffixFileFilter(".java");
                
                // get the list of files
                Collection<File> allJavaFiles = FileUtils.listFiles(dirChooser.getCurrentDirectory(), javaFilter, TrueFileFilter.INSTANCE);

                try {

                    // create a new file writer
                    FileWriter writer = new FileWriter("project.tex");
                    writer.append("\\documentclass{article}\n\n");
                    writer.append("\\usepackage[utf8]{inputenc}\n");
                    writer.append("\\usepackage[T1]{fontenc}\n\n");
                    writer.append("\\usepackage[top=1.5cm,left=1.5cm,bottom=1.5cm,right=1.5cm]{geometry}\n\n");
                    writer.append("\\usepackage{tocloft}\n\n");
                    writer.append("\\renewcommand\\cftsecfont{\\normalfont}\n");
                    writer.append("\\renewcommand\\cftsecpagefont{\\normalfont}\n");
                    writer.append("\\renewcommand{\\cftsecleader}{~\\cftdotfill{\\cftsecdotsep}}\n");
                    writer.append("\\renewcommand\\cftsecdotsep{\\cftdot}\n");
                    writer.append("\\renewcommand\\cftsubsecdotsep{\\cftdot}\n\n");
                    writer.append("\\usepackage{titlesec}\n\n");
                    writer.append("\\titlelabel{}\n\n");
                    writer.append("\\makeatletter\n");
                    writer.append("\\let\\numberline\\@gobble\n");
                    writer.append("\\makeatother\n\n");
                    writer.append("\\usepackage{xcolor}\n");
                    writer.append("\\definecolor{bluekeywords}{rgb}{0.13,0.13,1}\n");
                    writer.append("\\definecolor{greencomments}{rgb}{0,0.5,0}\n");
                    writer.append("\\definecolor{redstrings}{rgb}{0.9,0,0}\n\n");
                    writer.append("\\usepackage[scaled]{beramono}\n\n");
                    writer.append("\\usepackage{listingsutf8}\n\n");
                    writer.append("\\author{Paulo Roberto Massa Cereda}\n");
                    writer.append("\\date{\\today}\n");
                    writer.append("\\title{" + title + "}\n\n");
                    writer.append("\\begin{document}\n\n");
                    writer.append("\\maketitle\n\n");
                    writer.append("\\tableofcontents\n\n");
                    writer.append("\\lstset{%\n");
                    writer.append("inputencoding=utf8/latin1,");
                    writer.append("language=Java,\n");
                    writer.append("basicstyle=\\small\\ttfamily,\n");
                    writer.append("showspaces=false,\n");
                    writer.append("numbers=left,\n");
                    writer.append("numberstyle=\\color{darkgray}\\tiny\\ttfamily,\n");
                    writer.append("showtabs=false,\n");
                    writer.append("breaklines=true,\n");
                    writer.append("showstringspaces=false,\n");
                    writer.append("breakatwhitespace=true,\n");
                    writer.append("commentstyle=\\color{greencomments},\n");
                    writer.append("keywordstyle=\\color{bluekeywords},\n");
                    writer.append("stringstyle=\\color{redstrings}\n");
                    writer.append("}\n\n");

                    // for all files
                    for (File x : allJavaFiles) {
                        
                        // write the section name and the input listing
                        writer.append("\\section{\\texttt{" + x.getAbsolutePath().replace(dirChooser.getCurrentDirectory().getAbsolutePath() + File.separator + dirChooser.getSelectedFile().getName() + File.separator, "").replaceAll("\\\\", "/") + "}}\n\n");
                        writer.append("\\lstinputlisting{" + x.getAbsolutePath().replaceAll("\\\\", "/") + "}\n\n");
                    }

                    // finalize document
                    writer.append("\\end{document}\n");
                    writer.close();

                    // show message
                    JOptionPane.showMessageDialog(null, "Project scanned and mapped.", "Yay!", JOptionPane.INFORMATION_MESSAGE);

                } catch (Exception e) {
                    
                    // show error
                    JOptionPane.showMessageDialog(null, "Something terrible happened:\n\n" + e.getMessage(), "Oh no!", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                
                // show error
                JOptionPane.showMessageDialog(null, "No directory was selected, bye.", "Uh-oh!", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}
