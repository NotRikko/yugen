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

import { useUser } from "@/UserProvider";



const formSchema = z.object({
    userId: z.number(),
    postContent: z.string().max(280, "Post content must be at most 280 characters"),
    productId: z.number()
});

export default function CreatePost({}) {
    const {user} = useUser();

    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            userId: user.id,
            postContent: "",
            productId: 0,
        }
    })

    async function onSubmit(values: z.infer<typeof formSchema>) {
        try {
            const response = await fetch("http://localhost:8080/posts/create", {
                mode: "cors",
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(values),
            })
        } catch (error) {
            console.error("Error creating post", error)
        };
    }
    return (
        <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8  mx-auto py-8">
                <FormField
                    control={form.control}
                    name="postContent"
                    render={({ field }) => (
                    <FormItem>
                        <FormLabel>Post Content</FormLabel>
                        <FormControl>
                            <Input 
                                placeholder="Post Content"
                                type="textarea"
                                {...field} 
                            />
                        </FormControl>
                        <FormDescription>Describe your post.</FormDescription>
                        <FormMessage />
                    </FormItem>
                    )}
                />
                <FormField
                    control={form.control}
                    name="postContent"
                    render={({ field }) => (
                    <FormItem>
                        <FormLabel>Post Content</FormLabel>
                        <FormControl>
                            <Input 
                                placeholder="Post Content"
                                type="textarea"
                                {...field} 
                            />
                        </FormControl>
                        <FormDescription>Describe your post.</FormDescription>
                        <FormMessage />
                    </FormItem>
                    )}
                />
                    
            <Button type="submit">Post</Button>
            </form>
        </Form>
    )
}