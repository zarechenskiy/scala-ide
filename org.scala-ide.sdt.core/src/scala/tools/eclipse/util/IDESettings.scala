/*
 * Copyright 2005-2010 LAMP/EPFL
 */
// $Id$

package scala.tools.eclipse
package util

import scala.tools.eclipse.properties.SettingsAddOn
import scala.tools.nsc.Settings


object IDESettings {
  import ScalaPluginSettings._
  
  case class Box(name: String, userSettings: List[Settings#Setting])
  
  def shownSettings(s : Settings) : List[Box] = {
    import s._

    List(
        Box("Standard",
            List(deprecation, g, optimise, target, unchecked,
                 pluginOptions, nospecialization)),
        Box("Advanced",
            List(checkInit, Xchecknull, elidebelow,
                 Xexperimental, future, XlogImplicits,
                 Xmigration28, noassertions, nouescape, plugin, disable,
                 require, pluginsDir, Xwarnfatal, Xwarninit)),
        Box("Private",
            List(Xcloselim, Xdce, inline, Xlinearizer, Ynogenericsig, noimports,
                 selfInAnnots, Yrecursion, refinementMethodDispatch,
                 YmethodInfer, YdepMethTpes, Ywarndeadcode, Ybuildmanagerdebug))
        // BACK-2.8
        //Box("Presentation Compiler",
        //    List(YpresentationDebug, YpresentationVerbose, YpresentationLog, YpresentationReplay))
    )
  }
  
  def pluginSettings: List[Box] = {
    val Yplugininfo = BooleanSetting("-plugininfo", "Print Scala plugin debugging info")
    List(Box("Scala Plugin Debugging", List(Yplugininfo)))    
  }
  
  def tuningSettings: List[Box] = {
    List(
      Box("Editor Tuning", List(outputInClasspath, compileOnTyping, useContentOfEditor, alwaysCleanBuild, classBreakpoint, markOccurencesForSelectionOnly, markOccurencesTStrategy, timeOutBodyReq))
      , Box("Builder Tuning", List(markUnusedImports, ignoreErrorOnJavaFile))
      , Box("QuickFix Tuning", List(quickfixImportByText))
      , Box("Logging Tuning", List(tracerEnabled))
      , Box("Editor Debug", List(exceptionOnCreatePresentationCompiler))
    )
  }

  val outputInClasspath = BooleanSetting("_output in classpath", "append the outputs folders to the classpath (explicitly)", true)
  val compileOnTyping = BooleanSetting("_auto compile", "compile file on typing (else compile on save)", true)
  val useContentOfEditor = BooleanSetting("_editor content", "use content from Editor for compilation instead of saved file (may lock/freeze)", false)
  val alwaysCleanBuild = BooleanSetting("_clean+build", "always do a clean+full build", false)
  val classBreakpoint = BooleanSetting("_class breakpoint", "support toggling breakpoint on class from editor", false)
  val markOccurencesForSelectionOnly = BooleanSetting("_mark occurrences on selection", "doesn't try to mark occurrences if there is no selection", true)
  val markOccurencesTStrategy = {
    val choices = ScalaPlugin.plugin.updateOccurrenceAnnotationsService.strategies
    ChoiceSetting("_mark occurrences threading", "is which thread mark occurrences should run", choices , choices.head)
  }
  val timeOutBodyReq = IntSetting("_timeout body req", "timeout (ms) to access body/AST of a source file", 3000, Some((0,60000)), parseInt)
  val exceptionOnCreatePresentationCompiler = BooleanSetting("_exceptionOnCreatePresentationCompiler", "(for ScalaIDE debugging only) throw an exception when trying to create ScalaPresentationCompiler", false)

  val tracerEnabled = BooleanSetting("_tracer printing", "print tracer info on stdout/stderr", false)

  val markUnusedImports = BooleanSetting("_mark unused imports", "", true)
  val ignoreErrorOnJavaFile = BooleanSetting("_ignore error on java", "the scala builder should not report error about *.java", true)
  
  val quickfixImportByText = BooleanSetting("_import via text", "quick fix for import done by text manipulation (else by AST/refactoring)", false)

  def parseInt(s : String) : Option[Int] = {
    try {
      Some( s.toInt )
    } catch {
      case _ => None
    }
  }
}

object ScalaPluginSettings extends SettingsAddOn {
  val YPlugininfo = BooleanSetting("-plugininfo", "Enable logging of the Scala Plugin info")
}
