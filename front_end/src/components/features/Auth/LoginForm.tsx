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


const formSchema = z.object({
  username: z.string().min(4, "Username must be at least 4 characters").max(25, "Username must be at most 25 characters"),
  password: z
    .string()
    .min(8, "Password must be at least 8 characters long")
    .max(30, "Password must be at most 30 characters long"),
});

export default function LoginForm() {
    const form = useForm<z.infer<typeof formSchema>>({
      resolver: zodResolver(formSchema),
      defaultValues: {
        username: "",
        password: "",
      },
    })
   
    async function onSubmit(values: z.infer<typeof formSchema>) {
      try {
        const response = await fetch("http://localhost:8080/users/login", {
          mode: "cors",
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(values),
        });
    
        const data = await response.json();
    
        if (!response.ok) {
          throw new Error(data.message || "Login failed");
        }
    
        console.log("Login successful:", data);
      } catch (error) {
        console.error("Login error:", error);
      }
    }

    return (
      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8  mx-auto py-8">
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
                                {...field} 
                            />
                        </FormControl>
                        <FormDescription>Enter your username.</FormDescription>
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
                            <Input type="password" placeholder="Placeholder" {...field} />
                        </FormControl>
                        <FormDescription>Enter your password.</FormDescription>
                        <FormMessage />
                    </FormItem>
                    )}
                />
          <Button type="submit">Login</Button>
          <p className="text-xs">Don't have an account? Signup here.</p>
        </form>
      </Form>
    )
  }