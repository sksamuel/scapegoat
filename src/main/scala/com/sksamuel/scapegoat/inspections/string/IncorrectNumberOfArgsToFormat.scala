package com.sksamuel.scapegoat.inspections.string

import com.sksamuel.scapegoat._

/** @author Stephen Samuel */
class IncorrectNumberOfArgsToFormat extends Inspection("Incorrect number of args for format", Levels.Error) {

  // format is: %[argument_index$][flags][width][.precision][t]conversion
  //        OR: %%
  final val argRegex = "%((\\d+\\$)?[-#+ 0,(\\<]*?\\d?(\\.\\d+)?[tT]?[a-zA-Z]|%)".r

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def postTyperTraverser = Some apply new context.Traverser {

      import context.global._

      private def doesNotTakeArguments(formatSpecifier: String) = {
        formatSpecifier == "%%" || formatSpecifier == "%n"
      }

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Apply(Select(Apply(Select(_, TermName("augmentString")), List(Literal(Constant(format)))),
            TermName("format")), args) =>
            // %% doesn't consume any arguments, but all other formats do
            val argCount = argRegex.findAllIn(format.toString).matchData.filterNot(m => doesNotTakeArguments(m.matched)).size
            if (argCount > args.size)
              context.warn(tree.pos, self, tree.toString().take(500))
          case _ => continue(tree)
        }
      }
    }
  }
}
