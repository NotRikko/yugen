"use client"

import { useState } from "react"
import { zodResolver } from "@hookform/resolvers/zod"
import { useForm } from "react-hook-form"
import { z } from "zod"

import { Button } from "@/components/ui/button"
import {
  Form,
  FormControl,
  FormDescription,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form"
import { Input } from "@/components/ui/input"
import { Checkbox } from "@/components/ui/checkbox"

 
const formSchema = z.object({
  username: z.string().min(2).max(50),
  email: z.string().email().min(5),
  password: z.string().min(8).max(50),
  confirm: z.string(),
  isArtist: z.boolean(),
  artistName: z.string().min(2).max(50).optional(),
})
.refine((data) => data.password === data.confirm, {
    message: "Passwords don't match",
    path: ["confirm"],
})
.refine((data) => {
  if (data.isArtist && !data.artistName) return false;
  return true;
}, {
  message: "Artist name is required",
  path: ["artistName"],
});


export default function SignupForm() {
    const form = useForm<z.infer<typeof formSchema>>({
      resolver: zodResolver(formSchema),
      defaultValues: {
        username: "",
        email: "",
        password: "",
        confirm: "",
        isArtist: false,
        artistName: "",
      },
    })
    const [isArtist, setIsArtist] = useState(false);
   
    function onSubmit(values: z.infer<typeof formSchema>) {
      console.log(values)
    }

    return (
        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="flex flex-col gap-2">
            <FormField
              control={form.control}
              name="username"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Username</FormLabel>
                  <FormControl>
                    <Input placeholder="Example" {...field} />
                  </FormControl>
                  <FormDescription>
                    This is your username.
                  </FormDescription>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="email"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Email</FormLabel>
                  <FormControl>
                    <Input type="email" placeholder="example@gmail.com" {...field} />
                  </FormControl>
                  <FormDescription>
                    Please input your email.
                  </FormDescription>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="password"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Password</FormLabel>
                  <FormControl>
                    <Input type="password" placeholder="" {...field} />
                  </FormControl>
                  <FormDescription>
                    This is your password.
                  </FormDescription>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="confirm"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Confirm Password</FormLabel>
                  <FormControl>
                    <Input type ="password" placeholder="" {...field} />
                  </FormControl>
                  <FormDescription>
                    Please confirm password.
                  </FormDescription>
                  <FormMessage />
                </FormItem>
              )}
            />
            {isArtist ? 
               <FormField
               control={form.control}
               name="artistName"
               render={({ field }) => (
                 <FormItem>
                   <FormLabel>Artist Name</FormLabel>
                   <FormControl>
                     <Input placeholder="Artist" {...field} />
                   </FormControl>
                   <FormDescription>
                      Please type your artist name.
                   </FormDescription>
                   <FormMessage />
                 </FormItem>
               )}
             />
            :
            null}
            <FormItem className="space-y-2 flex flex-row items-center space-x-2 gap-2">
                <FormControl>
                    <Checkbox checked={isArtist} onCheckedChange={(checked) => setIsArtist(checked === true)}  />
                </FormControl>
                <FormLabel className="!m-0">Signup as Artist?</FormLabel>
            </FormItem>
            <Button type="submit">Submit</Button>
          </form>
        </Form>
      )
  }