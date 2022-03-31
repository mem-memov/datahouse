package memmemov.datahouse

import scalafx.Includes._
import scalafx.application.JFXApp3
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.canvas.Canvas
import scalafx.scene.effect.DropShadow
import scalafx.scene.layout.{HBox, Pane, Region, StackPane, VBox}
import scalafx.scene.paint.Color._
import scalafx.scene.paint._
import scalafx.scene.shape.{Box, Rectangle}
import scalafx.scene.text.{Font, Text}

object ScalaFXHelloWorld extends JFXApp3 {

  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title.value = "Hello Stage"
      width = 600
      height = 450
      scene = new Scene {
        new VBox(
          new HBox(
            new Pane {
              fill = Black
              getChildren.addOne(
                new Text("machine") {
                  x = 20
                  y = 50
                  stroke = Gray
                  font = new Font("Arial", 20)
                  fill <== when(hover) choose LightGray otherwise DarkGray
                }
              )

            }
          )
        )
      }

//        new VBox(
//        new HBox(
//          new Rectangle { r =>
//            fill = Black
//            onMouseClicked = event => {
//              r.parent.addcontent.add(
//                {
//                  new Text("machine") {
//                    x = event.getX
//                    y = event.getY
//                    stroke = Gray
//                    font = new Font("Arial", 20)
//                    fill <== when(hover) choose LightGray otherwise DarkGray
//                  }
//                }
//              )
//            }
//          }
//        )
//      )
    }
  }
}
