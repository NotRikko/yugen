"use client"

import { useForm } from "react-hook-form"
import {
  zodResolver
} from "@hookform/resolvers/zod"
import * as z from "zod"
import { Button } from "@/shared/ui/button"
import {
  Form,
  FormControl,
  FormDescription,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/shared/ui/form"
import { Input } from "@/shared/ui/input"
import { useNavigate } from "react-router-dom"
import { useUser } from "@/features/user/UserProvider"


const formSchema = z.object({
  username: z.string().min(4, "Username must be at least 4 characters").max(25, "Username must be at most 25 characters"),
  password: z
    .string()
    .min(8, "Password must be at least 8 characters long")
    .max(30, "Password must be at most 30 characters long"),
});

export default function LoginForm() {
    const navigate = useNavigate();
    const { handleLogin } = useUser();
    const form = useForm<z.infer<typeof formSchema>>({
      resolver: zodResolver(formSchema),
      defaultValues: {
        username: "",
        password: "",
      },
    })
   
    async function onSubmit(values: z.infer<typeof formSchema>) {
      try {
        await handleLogin(values.username, values.password);
        navigate("/feed"); 
      } catch (err: unknown) {
        console.error(err);
        alert(err.message || "Login failed"); 
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
          <p 
            className="text-xs cursor-pointer text-blue-500 hover:underline"
            onClick={() => navigate("/signup")}
          >
            Don't have an account? Signup here.
          </p>
          <div className="mt-4">
          <button
            type="button"
            className="px-3 py-1 rounded bg-gray-200 hover:bg-gray-300 text-sm"
            onClick={() => navigate("/")}
          >
            Home
          </button>
        </div>
        </form>
      </Form>
    )
  }