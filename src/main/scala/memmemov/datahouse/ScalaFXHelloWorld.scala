package memmemov.datahouse

import scalafx.Includes._
import scalafx.application.JFXApp3
import scalafx.beans.property.ObjectProperty
import scalafx.geometry.Insets
import scalafx.scene.{Scene, shape}
import scalafx.scene.canvas.Canvas
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.effect.DropShadow
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.{Background, BackgroundFill, CornerRadii, HBox, Pane, Region, StackPane, VBox}
import scalafx.scene.paint.Color._
import scalafx.scene.paint._
import scalafx.scene.shape.{Box, Rectangle}
import scalafx.scene.text.{Font, Text}

object ScalaFXHelloWorld extends JFXApp3 {

  override def start(): Unit = {
//
//    val pane = new Pane()
//
//    pane.onMouseClicked = (event: MouseEvent) =>
//      pane.getChildren.addOne(
//        new Text("machine") {
//          x = 10
//          y = 50
//          stroke = Gray
//          font = new Font("Arial", 20)
//          fill <== when(hover) choose LightGray otherwise DarkGray
//        }
//      )



    stage = new JFXApp3.PrimaryStage {
      title.value = "Datahouse"
      width = 600
      height = 450
      scene = new Scene { s =>

        content = new Pane { p =>
          fill = Blue
          minWidth <== s.width
          minHeight <= s.height
          children = Seq(
            new Rectangle {
              fill = Black
              width = 100
              height = 50
              x = 120
              y = 30
            },
            new Rectangle {
              fill = Black
              width = 100
              height = 50
              x = 120
              y = 130
            },
            new Rectangle {
              fill = Black
              width = 100
              height = 50
              x = 220
              y = 330
            },
          )
          onMouseClicked = event =>
            getChildren.addOne(
              new Text("machine") {
                x = event.getX
                y = event.getY
                stroke = Gray
                font = new Font("Arial", 20)
                fill <== when(hover) choose LightGray otherwise DarkGray
              }
            )
            new Alert(AlertType.Information, s"Hello Dialogs!!! ${p.width.value}").showAndWait()

        }
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
