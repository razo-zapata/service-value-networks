/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package service_applet_v01;

/**
 *
 * @author ivan
 */
public class Tuple {

   private int Value;
   private int Position;

   public Tuple()
   {
   }

   public Tuple(int Position, int Value)
   {
      this.Value   = Value;
      this.Position = Position;
   }

   public int get_Value()
   {
      return Value;
   }

   public int get_Position()
   {
      return Position;
   }

   public void set_Value(int Value)
   {
      this.Value = Value;
   }

   public void set_Position(int Position)
   {
      this.Position = Position;
   }

}
