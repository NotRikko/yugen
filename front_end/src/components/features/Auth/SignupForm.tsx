"use client"

import {
  useForm
} from "react-hook-form"
import {
  zodResolver
} from "@hookform/resolvers/zod"
import * as z from "zod"
import {
  Button
} from "@/components/ui/button"
import {
  Form,
  FormControl,
  FormDescription,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form"
import {
  Input
} from "@/components/ui/input"
import {
  Checkbox
} from "@/components/ui/checkbox"
import { useNavigate } from "react-router-dom"

 
const formSchema = z.object({
  username: z.string().min(4, "Username must be at least 4 characters").max(25),
  displayName: z.string().min(4, "Display name must be at least 4 characters").max(25),
  email: z.string().email("Invalid email format"),
  password: z.string().min(8, "Password must be at least 8 characters").max(30),
  confirm: z.string().min(8),
  isArtist: z.boolean().default(false),
}).refine((data) => data.password === data.confirm, {
  path: ["confirm"],
  message: "Passwords don't match",
});

export default function SignupForm() {
    const navigate = useNavigate();
    const form = useForm<z.infer<typeof formSchema>>({
      resolver: zodResolver(formSchema),
      defaultValues: {
        username: "",
        displayName: "",
        email: "",
        password: "",
        confirm: "",
        isArtist: false,
      },
    })
   
    async function onSubmit(values: z.infer<typeof formSchema>) {
      try {
        const response = await fetch("http://localhost:8080/users/create", {
          mode: "cors",
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(values),
        });
    
        const data = await response.json();
    
        if (!response.ok) {
          Object.entries(data).forEach(([field, message]) => {
            if (form.getFieldState(field as keyof typeof values)) {
              form.setError(field as keyof typeof values, { type: "server", message: message as string });
            } else {
              form.setError("username", { type: "server", message: data.error || "Signup failed" });
            }
          });
          return;
        }
    
        navigate('/login')
      } catch (error) {
        console.error("Signup error:", error);
      }
    }

    return (
      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8  mx-auto py-8">
          <div className="grid grid-cols-12 gap-4">
            <div className="col-span-6">
              <FormField
                control={form.control}
                name="username"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Username</FormLabel>
                    <FormControl>
                      <Input 
                      placeholder="Username"
                      
                      type="text"
                      {...field} />
                    </FormControl>
                    <FormDescription>This is your username.</FormDescription>
                    <FormMessage />
                  </FormItem>
                )}
              />
            </div>
            <div className="col-span-6">
              <FormField
                control={form.control}
                name="displayName"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Display Name</FormLabel>
                    <FormControl>
                      <Input 
                      placeholder="Display"
                      
                      type="text"
                      {...field} />
                    </FormControl>
                    <FormDescription>This is your public display name.</FormDescription>
                    <FormMessage />
                  </FormItem>
                )}
              />
            </div>
          
          </div>
          <FormField
            control={form.control}
            name="email"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Email</FormLabel>
                <FormControl>
                  <Input 
                  placeholder="email@gmail.com"
                  
                  type="email"
                  {...field} />
                </FormControl>
                <FormDescription>This is your email.</FormDescription>
                <FormMessage />
              </FormItem>
            )}
          />
          <div className="grid grid-cols-12 gap-4">
            <div className="col-span-6">
              <FormField
                control={form.control}
                name="password"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Password</FormLabel>
                    <FormControl>
                      <Input type="password" placeholder="Placeholder" {...field} />
                    </FormControl>
                    <FormDescription>Enter your password.</FormDescription>
                    <FormMessage />
                  </FormItem>
                )}
              />
            </div>
            <div className="col-span-6">
              <FormField
                control={form.control}
                name="confirm"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Confirm Password</FormLabel>
                    <FormControl>
                      <Input type="password" placeholder="Placeholder" {...field} />
                    </FormControl>
                    <FormDescription>Confirm your password.</FormDescription>
                    <FormMessage />
                  </FormItem>
                )}
              />
            </div>
          </div>
          <FormField
            control={form.control}
            name="isArtist"
            render={({ field }) => (
              <FormItem className="flex flex-row items-start space-x-3 space-y-0 rounded-md border p-4">
                <FormControl>
                  <Checkbox
                    checked={field.value}
                    onCheckedChange={field.onChange}
                    
                  />
                </FormControl>
                <div className="space-y-1 leading-none">
                  <FormLabel>Sign up as an artist?</FormLabel>
                  <FormDescription>You can sign up as an artist to create products and post them.</FormDescription>
                  <FormMessage />
                </div>
              </FormItem>
            )}
          />
          <Button type="submit">Submit</Button>
          <p className="text-xs">Already have an account? Sign in here.</p>
        </form>
      </Form>
    )
  }